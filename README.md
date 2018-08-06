# Ascensores

En el diseño de este programa se nos solicita la simulación del movimiento de dos ascensores en un rascacielos de 20 plantas (incluyendo planta baja).

Para completar la simulación se crearán una serie de objetos. Son los siguientes:

			- Personas: se montarán en los ascensores y realizarán el ascenso o descenso.
      
			- Plantas: Son el origen y el destino de las personas. 
      
			- Ascensor: Medio de transporte que comunica las plantas entre sí. 
            Tiene una capacidad limitada a 8 personas y se irá estropeando cada cierto tiempo.
			
      - Motor: Mueve los ascensores a lo largo del rascacielos.
      
A su vez, existirán dos elementos remotos: el Vigilante y el Controlador. El primero tendrá será un modulo de consulta del estado de los ascensores, el cual mostrara el ascensor que esta en funcionamiento, su estado , cuantas personas lleva en su interior y los destinos de las personas. El segundo tendrá la opción de parar o reanudar el movimiento de los ascensores. 
