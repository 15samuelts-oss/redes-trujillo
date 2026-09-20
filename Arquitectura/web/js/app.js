const API_URL = 'http://localhost:8080/api/estaciones';

document.addEventListener('DOMContentLoaded', () => {
    iniciarDashboard();
});

async function iniciarDashboard() {
    const statusText = document.getElementById('status-text');
    const statusDot = document.getElementById('server-status');
    const grid = document.getElementById('stations-grid');

    try {
        const respuesta = await fetch(API_URL);
        if (!respuesta.ok) throw new Error('Fallo HTTP');
        
        const datos = await respuesta.json();
        
        statusText.textContent = "Backend Operativo";
        statusDot.classList.add('online');
        
        renderizarEstaciones(datos, grid);
    } catch (error) {
        console.error(error);
        statusText.textContent = "Error de Conexión";
        statusDot.classList.remove('online');
        grid.innerHTML = `<div style="grid-column: 1/-1; text-align:center; padding: 2rem; color: var(--status-error);"><h3>⚠️ Sistema Desconectado</h3></div>`;
    }
}

function renderizarEstaciones(estaciones, contenedorGrid) {
    contenedorGrid.innerHTML = ''; 

    estaciones.forEach(estacion => {
        const tarjeta = document.createElement('article');
        tarjeta.className = 'station-card';
        
        // [MODIFICADO]: El span ahora es un botón (button) interactivo
        const htmlPuntos = estacion.puntosDeCarga.map(punto => `
            <div class="point-item">
                <div class="point-info">
                    <strong>🔌 ${punto.id}</strong>
                    <span class="point-power">Potencia: ${punto.potenciaKw} kW</span>
                </div>
                <button 
                    class="status-badge ${punto.estado.toLowerCase()}"
                    onclick="cambiarEstado('${punto.id}', '${punto.estado}')"
                    title="Clic para alternar estado de la toma"
                >
                    ${punto.estado}
                </button>
            </div>
        `).join('');

        tarjeta.innerHTML = `
            <header class="station-header">
                <h2>${estacion.ubicacion}</h2>
                <span class="id-badge">NODO: ${estacion.id}</span>
            </header>
            <div class="point-list">
                ${htmlPuntos}
            </div>
        `;
        contenedorGrid.appendChild(tarjeta);
    });
}

// [NUEVO]: Controlador asíncrono para mutación de datos
async function cambiarEstado(idPunto, estadoActual) {
    // Alternancia de estado (Toggle lógico)
    const nuevoEstado = estadoActual === 'DISPONIBLE' ? 'OCUPADO' : 'DISPONIBLE';
    
    try {
        const respuesta = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: idPunto,
                estado: nuevoEstado
            })
        });

        if (respuesta.ok) {
            // Si el backend actualizó el POJO con éxito, recargamos la UI
            iniciarDashboard();
        } else {
            console.error("El backend rechazó la actualización de estado.");
        }
    } catch (error) {
        console.error("Fallo de red al intentar actualizar el punto:", error);
    }
}