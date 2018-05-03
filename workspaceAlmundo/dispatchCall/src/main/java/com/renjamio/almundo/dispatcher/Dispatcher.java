package com.renjamio.almundo.dispatcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import com.renjamio.almundo.model.Empleado;
import com.renjamio.almundo.model.Llamada;
import com.renjamio.almundo.model.Operador;
import com.renjamio.almundo.model.Supervisor;
import com.renjamio.exception.ExcepcionDispatch;

public class Dispatcher {

	private final static Logger LOGGER = Logger.getLogger("Dispatcher");

	private List<Empleado> empleadosDisponibles;
	private List<Empleado> empleadosOcupados;

	private List<Empleado> operadoresDisponibles;
	private List<Empleado> supervisoresDisponibles;
	private List<Empleado> directoresDisponibles;

	private Queue<Llamada> llamadasEnEspera;

	private static int cantLlamadasEnCurso = 0;

	public final int CANT_LLAMADAS_CONCURRENTES = 10;

	public Dispatcher() {
		super();
		operadoresDisponibles = new ArrayList<Empleado>();
		supervisoresDisponibles = new ArrayList<Empleado>();
		directoresDisponibles = new ArrayList<Empleado>();
		llamadasEnEspera = new LinkedList<Llamada>();

	}

	public synchronized void dispatchCall(Llamada llamada) throws ExcepcionDispatch, InterruptedException {
		// LOGGER.info("**** dispatchCall *****");
		Empleado empleadoLibre = getEmpleadoDisponible();
		if (empleadoLibre == null) {
			System.out.println("+++++++Llamada Encolada e hilo esperando+++++");
			encolarLlamada(llamada);
			wait();
			empleadoLibre = getEmpleadoDisponible();
		}
		asignarLlamadaAEmpleado(llamada, empleadoLibre);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Empleado asignado " + empleadoLibre + " asignado a llamada " + llamada);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");

	}

	private void encolarLlamada(Llamada llamada) {
		llamadasEnEspera.add(llamada);
	}

	private Llamada desencolarLlamada(Llamada llamada) {
		if (llamadasEnEspera.isEmpty()) {
			return null;
		}
		return llamadasEnEspera.poll();
	}

	public synchronized void unDispatchCall(Llamada llamada) throws ExcepcionDispatch {
		desasignarEmpleadoALlamada(llamada);
		notify();
	}

	private void desasignarEmpleadoALlamada(Llamada llamada) {
		agregarEmpleadoDisponible(llamada.getEmpleadoAsignado());
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Empleado desasignado " + llamada.getEmpleadoAsignado() + " asignado a llamada " + llamada);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		llamada.setEmpleadoAsignado(null);
		setCantLlamadasEnCurso(getCantLlamadasEnCurso() - 1);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("LLAMADAS EN CURSO " + cantLlamadasEnCurso);
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	private void asignarLlamadaAEmpleado(Llamada llamada, Empleado empleadoLibre) throws ExcepcionDispatch {
		empleadoLibre.setLlamadaAsignada(llamada);
		llamada.setEmpleadoAsignado(empleadoLibre);
		eliminarDeDisponibles(empleadoLibre);
		setCantLlamadasEnCurso(getCantLlamadasEnCurso() + 1);
		System.out.println("LLAMADAS EN CURSO " + cantLlamadasEnCurso);
	}

	private void eliminarDeDisponibles(Empleado empleadoLibre) throws ExcepcionDispatch {
		if (empleadoLibre.getTipoEmpleado() instanceof Operador) {
			operadoresDisponibles.remove(empleadoLibre);
		} else if (empleadoLibre.getTipoEmpleado() instanceof Supervisor) {
			supervisoresDisponibles.remove(empleadoLibre);
		} else {
			directoresDisponibles.remove(empleadoLibre);
		}

	}

	private Empleado getEmpleadoDisponible() {
		if (!operadoresDisponibles.isEmpty()) {
			return operadoresDisponibles.get(0);
		}
		if (!supervisoresDisponibles.isEmpty()) {
			return supervisoresDisponibles.get(0);
		}
		if (!directoresDisponibles.isEmpty()) {
			return directoresDisponibles.get(0);
		}
		return null;
	}

	public void agregarEmpleadoDisponible(Empleado empleado) {
		System.out.println(" DEBUGGGGG" + empleado);
		if (empleado.getTipoEmpleado() instanceof Operador) {
			operadoresDisponibles.add(empleado);
		} else if (empleado.getTipoEmpleado() instanceof Supervisor) {
			supervisoresDisponibles.add(empleado);
		} else {
			directoresDisponibles.add(empleado);

		}
	}

	public List<Empleado> getEmpleadosDisponibles() {
		return empleadosDisponibles;
	}

	public void setEmpleadosDisponibles(List<Empleado> empleadosDisponibles) {
		this.empleadosDisponibles = empleadosDisponibles;
	}

	public List<Empleado> getEmpleadosOcupados() {
		return empleadosOcupados;
	}

	public void setEmpleadosOcupados(List<Empleado> empleadosOcupados) {
		this.empleadosOcupados = empleadosOcupados;
	}

	public List<Empleado> getOperadoresDisponibles() {
		return operadoresDisponibles;
	}

	public void setOperadoresDisponibles(List<Empleado> operadoresDisponibles) {
		this.operadoresDisponibles = operadoresDisponibles;
	}

	public List<Empleado> getSupervisoresDisponibles() {
		return supervisoresDisponibles;
	}

	public void setSupervisoresDisponibles(List<Empleado> supervisoresDisponibles) {
		this.supervisoresDisponibles = supervisoresDisponibles;
	}

	public List<Empleado> getDirectoresDisponibles() {
		return directoresDisponibles;
	}

	public void setDirectoresDisponibles(List<Empleado> directoresDisponibles) {
		this.directoresDisponibles = directoresDisponibles;
	}

	public static int getCantLlamadasEnCurso() {
		return cantLlamadasEnCurso;
	}

	public static void setCantLlamadasEnCurso(int cantLlamadasEnCurso) {
		Dispatcher.cantLlamadasEnCurso = cantLlamadasEnCurso;
	}

}
