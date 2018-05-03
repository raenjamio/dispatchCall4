package com.renjamio.almundo;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.renjamio.almundo.dispatcher.Dispatcher;
import com.renjamio.almundo.model.Director;
import com.renjamio.almundo.model.Empleado;
import com.renjamio.almundo.model.Llamada;
import com.renjamio.almundo.model.Operador;
import com.renjamio.almundo.model.Supervisor;
import com.renjamio.controller.HiloLlamada;
import com.renjamio.exception.ExcepcionDispatch;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Empleado empleadoOperador1 = new Empleado(new Operador(), "juan","perez");
        Empleado empleadoOperador2 = new Empleado(new Operador(), "carlos","sanchez");
        Empleado empleadoSupervisor1 = new Empleado(new Supervisor(), "maria","sanchez");
        Empleado empleadoSupervisor2 = new Empleado(new Supervisor(), "lorena","ortega");
        Empleado empleadoDirector1 = new Empleado(new Director(), "juana","ortega");
        
        Llamada llamada1 = new Llamada();
        Llamada llamada2 = new Llamada();
        Llamada llamada3 = new Llamada();
        Llamada llamada4 = new Llamada();
        Llamada llamada5 = new Llamada();
        Llamada llamada6 = new Llamada();
        Llamada llamada7 = new Llamada();
        Llamada llamada8 = new Llamada();
        
        
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.agregarEmpleadoDisponible(empleadoOperador1);
        dispatcher.agregarEmpleadoDisponible(empleadoOperador2);
        dispatcher.agregarEmpleadoDisponible(empleadoSupervisor1);
        dispatcher.agregarEmpleadoDisponible(empleadoSupervisor2);
        dispatcher.agregarEmpleadoDisponible(empleadoDirector1);
        
        Random aleatorio = new Random(System.currentTimeMillis());
        int intAletorio = aleatorio.nextInt(10)+5;
        
        HiloLlamada tarea1 = new HiloLlamada(10, dispatcher,llamada1);
		HiloLlamada tarea2 = new HiloLlamada(7, dispatcher,llamada2);
		HiloLlamada tarea3 = new HiloLlamada(5, dispatcher,llamada3);
		HiloLlamada tarea4 = new HiloLlamada(5, dispatcher,llamada4);
		HiloLlamada tarea5 = new HiloLlamada(5, dispatcher,llamada5);
		HiloLlamada tarea6 = new HiloLlamada(5, dispatcher,llamada6);
	
		tarea1.start();
		tarea2.start();
		tarea3.start();
		tarea4.start();
		try {
			TimeUnit.SECONDS.sleep(3);
			tarea5.start();
			tarea6.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 /*
		dispatcher.dispatchCall(llamada1);
		dispatcher.dispatchCall(llamada2);
		dispatcher.dispatchCall(llamada3);
		dispatcher.dispatchCall(llamada4);
		dispatcher.dispatchCall(llamada5);
		*/
        
        
    }
}
