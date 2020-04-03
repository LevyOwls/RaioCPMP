package structs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import clases.Column;
import clases.Field;

public class Node 
{
	private Field instancia=new Field();
	private static ArrayList nodos=new ArrayList();
	private ArrayList hijos=new ArrayList();
	private int movimientos;
	private double puntaje;
	private int indice;
	private int wFlag=0;
	//**************************************************************************************//
	/************************************GETTERS Y SETTERS*********************************/
	//**************************************************************************************//
	
	public int getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(int movimientos) {
		this.movimientos = movimientos;
	}
	public int getwFlag() {
		return wFlag;
	}
	public void setwFlag(int wFlag) {
		this.wFlag = wFlag;
	}
	public double getPuntaje() {
		return puntaje;
	}
	public void setPuntaje(double puntaje) {
		this.puntaje = puntaje;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	//**************************************************************************************//
	/****************************************************************************************/
	//**************************************************************************************//
	
	
	public Node(Field f,int indice)
	{
		this.instancia=f;
		this.indice=indice+1;
		this.wFlag=0;
		
	}
	
	
	/**
	 * GENERA EL PUNTAJE DE CADA INSTANCIA
	 */
	public void asignarPuntaje()
	{
		Node temp;
		int i=0;
		//PUNTAJE ACTUAL
		
		double k=0.9;
		double p=1.0;
		double pacum=1.0;
		//BEST MOVIMIENTOS ACTUAL=
		temp=(Node)hijos.get(i);
		int movimientos=-1;
		
		for (i=0;i<hijos.size();i++)
		{
			temp=(Node)hijos.get(i);
			//EN CASO DE QUE TENGAN LA MISMA CANTIDAD DE MOVIMIENTOS SE LES ASIGNA EL MISMO PUNTAJE
			if (temp.getMovimientos()==movimientos)
			{
				temp.setPuntaje(p);
				pacum = k*pacum;
			}

			//EN CASO QUE SEAN MAS MOVIMIENTOS, SE REDUCE EL VALOR DE K EN FUNCION A LA CANTIDAD DE 
			//IGUALES EN LOS CASOS ANTERIORES
			if (temp.getMovimientos()>movimientos)
			{
				p=pacum;
				//ESTO ES PORQUE EN CASO QUE SE DEN HASTA 999 HIJOS CON K-INICIAL DE PUNTAJE
				p=k*p;
				pacum=p;
				temp.setPuntaje(p);
			}
			movimientos = temp.getMovimientos();
			
		}
		
	}
	
	/**
	 * RESUELVE LA COPIA DE LA INSTANCIA Y RETORNA LOS MOVIMIENTOS QUE DEMORO
	 * @return		CANTIDAD DE MOVIMIENTOS EN LOS QUE SE RESOLVIO LA INSTANCIA
	 * @throws IOException
	 */
	public int solve() throws IOException
	{
		return instancia.solve();
	}
	
		
	/**
	 * 
	 * @param cotaSuperior					VARIABLE EXCLUYENNTE, CONSIDERANDO EL DOMINIO DE LOS POSIBLES NUMEROS RANDOM EN [0 ,(N-1)[
	 * @return
	 */
	public  int random(int cotaSuperior)
	{
		int h= (int) Math.floor(Math.random()*cotaSuperior);
		return h;
		
		
	}

	/**
	 * 
	 * @param l			ARRAYLIST A MODIFICAR
	 * @param puntaje	PUNTAJE OBTENIDO
	 * @param cantidad	CANTIDAD DE VECES QUE SE ESCRIBE
	 * @return
	 */
	public ArrayList insertarRuleta(ArrayList l,double puntaje, double cantidad)
	{
		cantidad=cantidad*1000;
		cantidad=Math.round(cantidad);
		
		//SI LA LISTA ESTA VACIA SE AGREGAN TODOS DE CORRIDO
		if (l.isEmpty())
		{
			
			while (cantidad!=0)
			{
				l.add(puntaje);
				cantidad--;
			}
		}
		//EN CASO QUE NO SE 
		else
		{
			//MOUSEKERRAMIENTA MISTERIOSA QUE NOS AYUDARA MAS TARDE
			int random;
			
			while (cantidad!=0)
			{
				random=random(l.size()-1);
				l.add(random, puntaje);
				cantidad--;
			}
		}
		
		return l;
	}
	
	/**
	 * ELIMINA TODOS LOS HIJOS EXCEPTO EL DE LA INSTANCIA
	 * @param mantener
	 */
	public void eliminarHijos(Node mantener)
	{
		
		//SE ELIMINA TODO
		hijos.clear();
		
		//PERO SE AGREGA EL HIJO
		hijos.add(mantener);
	}
	
	/**
	 * ELIMINA LOS HIJOS QUE TENGAN MENOR PUNTAJE.
	 * @param k				puntaje minimo
	 */
	public void filtrarHijos(double k)
	{
		Node aux;
		int i;
		int n=0;
		for (i=0;i<hijos.size();i++)
		{
			aux=(Node)hijos.get(i);
			if (aux.getPuntaje()<k)
			{
				n=i;
				break;
			}
		}
		n=hijos.size()-n-1;
		while(n!=0)
		{
			hijos.remove(hijos.size()-1);
			n--;
		}
	}
	
	/**
	 * ORDENA DE MENOR A MAYOR, EN ESTE CASO MOVIMIENTOS
	 * @param size
	 * @param columna
	 * @return
	 */
	public  ArrayList bubbleSort(ArrayList size,ArrayList columna)
	{
		 boolean sorted=false;
		 int i;
		 Object aux;
		 while (!sorted)
		 {
			 sorted = true;
			 for (i=0;i<size.size()-1;i++)
			 {
				 //SI I>I+1 CAMBIAN DE POSICION
				 if ((int)size.get(i)>(int)size.get(i+1))
				 {
					 //SE ORDENA POR TAMAÑO
					 aux=(int)size.get(i);
					 size.set(i,size.get(i + 1));
					 size.set(i + 1,aux);
					 //SE ORDENA POR COLUMNA
					 aux=(Node)columna.get(i);
					 columna.set(i,columna.get(i + 1));
					 columna.set(i + 1,aux);
					 
					 sorted=false;
				 }
			 }
		 }
		 
		 return columna;
	}
	
	/**
	 * UN SELECT HIJO PERO MAS BACAN
	 * @return
	 */
	public Node roulleteMaker()
	{
		double aux;
		double total=0.0;
		int i;
		Node temp;
		ArrayList nonRepeated=new ArrayList();
		
		for (i=0;i<hijos.size();i++)
		{
			temp=(Node)hijos.get(i);
			aux=temp.getPuntaje();
			//SI EL PUNTAJE NO ESTA REPETIDO, ENTONCES SE AÑADE A LA LISTA
			if (!nonRepeated.contains(aux))
			{
				nonRepeated.add(aux);
			}
		}
		
		//UNA VEZ REALIZADA LA LISTA SE HACE LA SUMA TOTAL DE LOS PUNTAJES SIN REPETIR
		for (i=0;i<nonRepeated.size();i++)
		{
			aux=(double)nonRepeated.get(i);
			total+=aux;
		}
		//AHORA SE CALCULA UN PORCENTAJE 
		
		ArrayList ruleta=new ArrayList();
		double porcentaje;
		
		for (i=0;i<nonRepeated.size();i++)
		{
			aux=(double)nonRepeated.get(i);
			porcentaje=aux/total;
			//ArrayList,Puntaje,Posibilidad de que aparezca
			ruleta=insertarRuleta(ruleta, aux, porcentaje);
		}
		
		//UNA VEZ ARMADA LA RULETA, SE OBTIENE AL AZAR EL NODO SIGUIENTE

		//UNA VEZ RELLENADA LA RULETA, SE SELECCIONA UN NUMERO
		
		//COMO SE AGREGARON POR PORCENTAJE, SE OBTIENEN AL AZAR
		int seleccion=random(ruleta.size()-1);
		
		aux=(double) ruleta.get(seleccion);
		
		//UNA VEZ OBTENIDO EL PUNTAJE A OBTENER, SE REVISA CUANTOS CUMPLEN CON EL CRITERIO
		
		
		ArrayList sorteo=new ArrayList();
		
		for (i=0;i<hijos.size();i++)
		{
			temp=(Node)hijos.get(i);
			
			//SI CORRESPONDE CON EL PUNTAJE OBTENIDO EN LA RULETA, SE GUARDA EN SORTEO
			if (temp.getPuntaje()==aux)
			{
				sorteo.add(temp);
			}
			
		}
		
		//SE GENERA UN RANDOM
		seleccion=random(sorteo.size()-1);
		
		temp=(Node)sorteo.get(seleccion);
		nodos.add(temp);
		//SE ELIMINAN LOS HIJOS NO OCUPADOS PARA AHORRA MEMORIA.
		eliminarHijos(temp);
	
		return temp;
	
		
	}
	
	/**
	 * FUNCION ENCARGADA DE GENERAR TODOS LOS HIJOS POSIBLES DEL NODO
	 * @throws IOException
	 */
	public void generarHijos() throws IOException
	{
				

		//SE GENERA UNA BASE DE LA INSTANCIA ACTUAL (NODO PADRE).
		instancia.writeBase("src\\extras\\file");
		File dir=new File("src\\extras\\file");
		int i,j;
		Column temp;
		
		//POR CADA COLUMNA
		for (i=0;i<instancia.size();i++)
		{
			//MOVIMIENTOS POSIBLES
			for (j=0;j<instancia.size();j++)
			{
				//SE OBTIENE LA COLUMNA ORIGEN.
				temp=(Column)instancia.get(i);
				
				//SI LA COLUMNA NO TIENE NADA QUE SACAR (ESTA VACIA), PARA QUE NO TIRE ERROR ENTONCES SE OMITE
				if (temp.isEmpty())
				{
					break;
				}
				
				//SI J==I ENTONCES QUEDA EL MISMO NODO PADRE, LA IDEA ES GENERAR A SUS POSIBLES HIJOS
				if (j!=i)
				{
					temp=(Column)instancia.get(j);
					
					//SI ESTA LLENA NO SE LE PUEDE COLOCAR NADA Y SI ESTA VACIA NO SE PUEDE SACAR NADA DE ALLI
					if (temp.size()!=instancia.getHMax())
					{
						Field nuevo=new Field();
						//SE CREA LA COPIA
						nuevo.readFile(dir);
						//System.out.println();
						//nuevo.showTime();
						//System.out.println();
						//SE REALIZA EL MOVIMIENTO CORRESPONDIENTE
						/**AQUI ESTA TIRANDO UN ERROR**/
						nuevo.atomic_Move(i,j);
						//SE ACTUALIZA LOS VALORES DE LA INSTANCIA CON REVIEW
						nuevo.review();
						//CREO UN NUEVO NODO, DECLARANDO EL NODO PADRE Y LA INSTANCIA NUEVA
						Node hijo=new Node(nuevo,getIndice());
				
						

						//SE EVALUA 
						int bestMovs=1000000000;
						int hh;
						int auxiliar;
						for (hh=0;hh<5;hh++)
						{
							auxiliar=nuevo.solve();
							
							if (auxiliar<bestMovs)
							{
								bestMovs=auxiliar;
							}
						}
						hijo.setMovimientos(bestMovs);
						
						//hijo.setMovimientos(hijo.solve());
						
						//SE INGRESA A LOS HIJOS UNICAMENTE SI LOS MOVIMIENTOS QUE DEMORA ES MENOR AL PADRE
						if (hijo.getMovimientos()<getMovimientos())
						{
							hijos.add(hijo);
						}
						
					}
				}
			}
			
			
		}
		//UNA VEZ INGRESADOS TODOS LOS HIJOS, SE ORDENA DE ACUERDO A SU PUNTAJE
		ArrayList movs=new ArrayList();
		Node aux;
		for (i=0;i<hijos.size();i++)
		{
			aux=(Node)hijos.get(i);
			movs.add(aux.getMovimientos());
		}
		
		hijos=bubbleSort(movs, hijos);
		
		if (hijos.size()!=0)
		{
			//System.out.println("\nMOVIMIENTOS: "+getIndice()+"\n");
			//showtime();
			asignarPuntaje();
		}
	
		else
		{
			
			setwFlag(1);
		}
		//showtime2();
	}

	public boolean complete()
	{
		if (instancia.isOrdenado())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Node getLast()
	{
		int size=nodos.size();
		return (Node) nodos.get(size-1);
		
	}
	
	public void showtime()
	{
		instancia.showTime();
	}
	
	public void showtime2()
	{
		int i;
		Node aux;
		for (i=0;i<hijos.size();i++)
		{
			aux=(Node)hijos.get(i);
			System.out.println(aux.getMovimientos()+"\t"+aux.getPuntaje());
		}
		
	}
}
