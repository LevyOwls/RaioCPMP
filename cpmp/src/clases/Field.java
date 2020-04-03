package clases;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.sound.sampled.Line;

public class Field 
{
	private ArrayList field=new ArrayList();
	private static int hMax=5;
	private static int movs=0;
	private static int columns;
	private static ArrayList movimientos=new ArrayList();
 
	public void setField(ArrayList l)
	{
		field=l;
	}
	//******************COPIA EL CAMPO ACTUAL

	public int getHMax()
	{
		return hMax;
		
	}
	
	public void setHMax(int h)
	{
		hMax=h;
	}
	/**
	 * INGRESA LA LECTURA DE UN ARCHIVO A FIELD
	 * @param f file
	 * @throws FileNotFoundException
	 */
	public void readFile(File f) throws FileNotFoundException
	{
		field.clear();
		int num;
		int columns;
		int containers;
		int q;
		
		 try  (Scanner entrada = new Scanner(f)) 
		    {

		    	//Primero estan las columnas y los contenedores totales.
		        columns = entrada.nextInt();
		        containers=entrada.nextInt();
		        
		       // System.out.println("Entrada: \nColumnas:"+ columnas+" Contenedores: "+contenedores);

		        while (entrada.hasNextInt()) 
		        {
		        	q = entrada.nextInt(); //La cantidad de contenedores de dicha columna
		        	int i;
		        	Column nuevo=new Column();
		        	for (i=0;i<q;i++)
		        	{
		        		num=entrada.nextInt();
		        		nuevo.add(num);
		        		//System.out.print(numero + " "); //se muestra la prioridad de los contenedores de la columna
		        	}
		        	field.add(nuevo);
		        	//System.out.print("\n");
		        	//System.out.println(nuevo);
		         }
		       
		    }
		 
		 
	}
	public  int getMovs() {
		return movs;
	}

	public static void setMovs(int movs) {
		Field.movs = movs;
	}
	/**
	 * IMPRIME EL ESTADO ACTUAL DE FIELD
	 */

	public  void showTime()
	{
		int i;
		Column temp;
		for (i=0;i<field.size();i++)
		{	
			temp=(Column)field.get(i);
			System.out.println(temp+"  "+temp.isLock());
		}
	}
	
	/**
	 * ACTUALIZA EL ESTADO ACTUAL DE LAS COLUMNAS 
	 */
	public  void review()
	{
		int i;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			temp.generaIdeal();
			temp.setDif();
			temp.setuB();
			temp.setLock();
			
		}
	}
	
	
	/**
	 * GENERA UNA LISTA CON LA PRIORIDAD DE LOS CONTENEDORES DESORDENADOS, SIN REPETIR PRIORIDADES.
	 * @return lista con prioridades de contenedores desordenados.
	 */
	public  ArrayList disorderlyElements()
	{
		ArrayList elements=new ArrayList();
		
		Column temp;
		
		int i;
		int j;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI LA COLUMNA NO ESTA ORDENADA
			if (!temp.isLock())
			{
				for (j=0;j<temp.size();j++)
				{
					elements.add(temp.get(j));
				}
			}
		}
		Collections.sort(elements);
		
		ArrayList unrepeated=new ArrayList();
		int helper;
		for (i=0;i<elements.size();i++)
		{
			helper=(int)elements.get(i);
			if (!unrepeated.contains(helper))
			{
				unrepeated.add(helper);
			}
		}
		
		return unrepeated;
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
	 * @return ARRAYLIST CON LA PRIORIDAD DE LOS CONTENEDORES EN LA CIMA DE CADA COLUMNA
	 */
	public  ArrayList getTops()
	{
		ArrayList tops=new ArrayList();
		int i;
		int aux;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			if (!temp.isEmpty())
			{
				aux=(int)temp.get(temp.size()-1);
				tops.add(aux);
			}
			else
			{
				tops.add(0);
			}
		}
		return tops;
	}
	
	
	/**
	 * REVISA LAS COLUMNAS PARA DEFINIR SI ESTA ORDENADO O NO
	 * @return				TRUE EN CASO DE QUE FIELD ESTE ORDDENADO, FALSE EN CASO CONTRARIO
	 */
	public  boolean isOrdenado()
	{
		Column temp;
		int i;
		
		for (i=0;i<field.size();i++)
		{
			
			temp=(Column)field.get(i);
			if (!temp.isLock())
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * REVISA SI PUEDE COLOCARSE SOBRE ALGUNA COLUMNA
	 * @return 		NUMERO DE COLUMNA EN LA CUAL PUEDE POSICIONARSE O -1 EN CASO DE NO ENCONTRAR NINGUNA
	 */
	public  int posibleMove(int n,int c_o)
	{
		ArrayList tops=getTops();
		int i;
		int aux;
		int dif=n;
		int col=-1;
		Column temp;
		ArrayList desordenados=disorderlyElements();
		int bigDis=(int)desordenados.get(desordenados.size()-1);
		for (i=0;i<tops.size();i++)
		{
			aux=(int)tops.get(i);
			if (n<=aux)
			{
				if (dif>aux-n)
				{
					temp=(Column)field.get(i);
					//QUE NO SEA LA COLUMNA ORIGEN, QUE TENGA ESPACIO DISPONIBLE Y QUE ESTE ORDENADA
					if (temp.size()<hMax && i!=c_o && temp.isLock())
					{
						col=i;
						break;
					}
					
				}
			}
			//COLUMNA VACIA
			else
			{
				if (aux==0 && n==bigDis)
				{
					col=i;
					break;
				}
			}
			
			
		}
		return col;
	}
	
	/**
	 * MUEVE EL ELEMENTO SUPERIOR DE LA COLUMNA ORIGEN A LA COLUMNA DESTINO, NO VERIFICA ALTURA
	 * @param c_o COLUMNA ORIGEN
	 * @param c_d COLUMNA DESTINO
	 */
	public  void atomic_Move(int c_o,int c_d) 
	{
		
		//RECORDAR QUE EL MOVIMIENTO SERA 0 A 1, 2 A 3, 4 A 5 ETC ETC
		
		//SE GENERA UNA LISTA CON LOS MOVIMIENTOS HECHOS
		movimientos.add(c_o);
		movimientos.add(c_d);
		Column temp=(Column)field.get(c_o);
		int aux=(int)temp.quitar();
		temp=(Column)field.get(c_d);
		temp.add(aux);
		movs++;
		review();
		
		
	}	
	
	
	public  int totalContenedores()
	{
		int i;
		int sum=0;
		Column temp;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			sum=sum+temp.size();
		}
		return sum;
	}
	

	
	/**
	 * FUNCION QUE PERMITE CONOCER SI EXISTEN ESPACIOS VACIOS DENTRO DE FIELD
	 * @return					RETORNA -1 EN CASO DE NO EXISTIR, EN CASO CONTRARIO RETORNA EL NUMERO DE LA COLUMNA VACIA.
	 */
	public  int firstEmpty()
	{
		int i;
		Column temp;
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			if (temp.isEmpty())
			{
				return i;
			}
		}
		
		return -1;
	}
	//**********************************************************************************************************************
	
	/**
	 * 
	 * @param c_o		COLUMNA ORIGEN
	 * @return			MEJOR COLUMNA DE DONDE SACAR UN ELEMENTO PARA RELLENAR LA COLUMNA c_o
	 * 					EN CASO DE QUE NO SE PUEDA, RETORNAR -1
	 */
	public  int columnBestFill(int c_o)
	{
		ArrayList tops=getTops();
		
		int i;
		int aux;
		Column temp;
		int dif=1000;
		//SE OBTIENE EL TOP DE LA COLUMNA ORIGEN
		int columnTop=(int)tops.get(c_o);
		int bestCol=-1;
		//EN CASO DE QUE LA COLUMNA NO ESTE VACIA
		if (columnTop!=0)
		{
			for (i=0;i<tops.size();i++)
			{
				temp=(Column)field.get(i);
				//QUE NO ANALIZE LA COLUMNA DE ORIGEN
				if (i!=c_o)
				{
					aux=(int)tops.get(i);
					//EN CASO DE QUE AUX SEA MENOR A COLUMNTOP, ESTE DESORDENADO Y NO SEA 0 (RECORDAR QUE 0 ES VACIO)
					if (aux<=columnTop && !temp.isLock() && aux!=0)
					{
						//SI LA DIFERENCIA ANTERIOR ES MAYOR A LA DIFERENCIA ACTUAL
						if (dif>(columnTop-aux))
						{
							bestCol=i;
							dif=columnTop-aux;
						}
					}
					/*if(aux<=columnTop && temp.size()==1)
					{
						//SI LA DIFERENCIA ANTERIOR ES MAYOR A LA DIFERENCIA ACTUAL
						if (dif>(columnTop-aux))
						{
							bestCol=i;
							dif=columnTop-aux;
						}
					}*/
				}
			}
		}
		else
		{
			int big=0;
			for (i=0;i<tops.size();i++)
			{
				temp=(Column)field.get(i);
				//QUE NO ANALIZE LA COLUMNA DE ORIGEN
				if (i!=c_o)
				{
					aux=(int)tops.get(i);
					//AUX DEBE SER MAYOR EL MAYOR DENTRO DE LOS DESORDENADOS.
					if (!temp.isLock() && aux>big)
					{
						big=aux;
						bestCol=i;
					}
				}
			}
			
		}

		
		return bestCol;
	}
	
	/**
	 * MEDIANTE BUUBLESORT ORDENA COLUMNAS POR TAMAÑO DE MENOR A MAYOR
	 * 
	 * @param size			ARRAYLIST CON TAMAÑO DE CADA COLUMNA
	 * @param columna		ARRAYLIST CON EL NUMERO DE COLUMNA
	 * @return				ARRAYLIST CON NUMERO DE COLUMNAS ORDENADA DE MENOR A MAYOR
	 */
	public  ArrayList bubbleSort(ArrayList size,ArrayList columna)
	{
		 boolean sorted=false;
		 int aux,i;
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
					 aux=(int)columna.get(i);
					 columna.set(i,columna.get(i + 1));
					 columna.set(i + 1,aux);
					 
					 sorted=false;
				 }
			 }
		 }
		 
		 return columna;
	}
	
	/**
	 * DEFINE SI AIOFILL PUEDE REALIZARSE
	 * @return							TRUE SI PUEDE REALIZARSE OTRA ITERACION, FALSE EN CASO CONTRARIO
	 */
	public  boolean isFillPosible ()
	{
		Column temp;
		int i;
		int aux;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//SI LA ALTURA ES MENOR A HMAX Y ESTA ORDENADO
			if (temp.size()<hMax && temp.isLock())
			{
				 aux=columnBestFill(i);
				 //EN CASO DE QUE SE PUEDA REALIZAR UN MOVIMIENTO
				 if (aux!=-1)
				 {
					 //REALIZA EL MOVIMIENTO Y VUELVE A INTENTAR CON LA PRIMERA COLUMNA
					 return true;
				 }
			}
		}
		return false;
	}

	
	/**
	 * RETORNA LA MEJOR COLUMNA DESTINO PARA APOYAR UN CONTENEDOR, ESTA SE EVALUA OBSERVANDO QUE SEA MENOR AL CONTENEDOR QUE SE LE QUIERE PONER ENCIMA Y LA DIFERENCIA SEA INFIMA
	 * @param c_o 		COLUMNA ORIGEN
	 * @return 			MEJOR UBICACION POSIBLE PARA RETIRAR EL CONTENEDOR DE LA COLUMNA
	 */
	public  int bestVacateC_d(int c_o)
	{
		
		int i;
		ArrayList tops=getTops();
		Column temp;
		int coTop=(int)tops.get(c_o);
		int col=-1;
		int dif=200;
		int aux;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			if (i!=c_o && temp.size()<hMax)
			{
				aux=(int)tops.get(i);
				if (coTop>=aux)
				{
					if ((coTop-aux)<dif)
					{
						col=i;
					}
				}
				
			}
		}
		
		return col;
	}

	
	/**
	 * BUSCA LA CANTIDAD DE CONTENEDORES QUE SE LE PUEDEN SOBREPONER CON LOS TOPS ACTUALES
	 * @param c_o				COLUMNA ORIGEN
	 * @return					CANTIDAD DE CONTENEDORES DISPONIBLES PARA SOBREPONERSE
	 */
	public  int Contador(int c_o)
	{
		int cont=0;
		int i,aux;
		Column temp=(Column)field.get(c_o);
		
		//NUMERO AL CUAL SE LE QUIERE BUSCAR LA CANTIDAD DE CONTENEDORES QUE SE PUEDE SOBREPONER
		
		
		int top=(int)temp.get(temp.size()-1);
		
		for (i=0;i<field.size();i++)
		{
			//COLUMNA ACTUAL
			temp=(Column)field.get(i);
			
			//SI LA COLUMNA ACTUAL ESTA DESORDENADA, NO ES LA COLUMNA ORIGEN Y NO ESTA VACIA
			if (!temp.isLock() && i!=c_o && !temp.isEmpty())
			{
				//AUX TOMA EL VALOR DE EL CONTENEDOR TOP 
				aux=(int)temp.get(temp.size()-1);
			
				//EN CASO DE QUE SEA MENOR AL TOP DE C_O, SE CONSIDERA UN CANDIDATO DISPONIBLE
				if (aux<=top)
				{
					cont++;
				}
			}
		}
		
		return cont;
	}
	

	
	
	public static int dif(int n1,int n2)
	{
		if (n1>=n2)
		{
			return n1-n2;
		}
		
		return n2-n1;
		
	}

	/**
	 * VE CUAL ES EL MEJOR POSIBLE MOVIMIENTO  B-B CONSIDERANDO LA COLUMNA ACTUAL
	 * @param c_o	COLUMNA ORIGEN
	 * @return		-1 EN CASO DE NO ENCONTRAR, EL N° DE LA COLUMNA EN CASO DE HAYAR ALGUNA
	 */
	public  int badMove(int c_o)
	{
		int i;
		Column temp=(Column)field.get(c_o);
		int menorDif=100000;
		int dif,aux;
		int col=-1;
		int top=(int)temp.get(temp.size()-1);
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			if (i!=c_o && temp.size()<hMax)
			{
				aux=(int)temp.get(temp.size()-1);
				if (dif(aux,top)<menorDif)
				{
					menorDif=dif(aux,top);
					col=i;
				}
			}
		}
		return col;
	
	}
	
	//IMPRIME UN LISTADO CON LOS MOVIMIENTOS REALIZADOS
	public  void printMovs()
	{
		int i;
		System.out.println();
		for (i=0;i<movimientos.size();i++)
		{
			System.out.println(movimientos.get(i));
		}
	}
	
	
	public  void excelShowTime()
	{
		int i;
		int j;
		Column temp;
		int aux;
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			
			for (j=0;j<temp.size();j++)
			{
				aux=(int)temp.get(j);
				System.out.print(aux+"\t");
			}
			
			System.out.println();
			
		}
	}
	
	public  void verificar()
	{
		Collections.reverse(movimientos);
		int i;
		int aux,aux2;
		int movsize=movimientos.size();
		for (i=0;i<movsize;i++)
		{
			
			aux=(int)movimientos.get(i);
			i++;
			aux2=(int)movimientos.get(i);
			//			0	1
			atomic_Move(aux,aux2);
			//2
			
			
			if (i>=movimientos.size())
			{
				break;
			}
		}
		
	}
	
	/***************************************************************/
	/**************************RANDOM FUNCTIONS*********************/
	
	public  void RAIOfill()
	{

		int i;
		Column temp;
		ArrayList size=new ArrayList();
		ArrayList columna=new ArrayList();
		//SE LEE EL TAMAÑO Y LA COLUMNA 
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			size.add(temp.size());
			columna.add(i);
		}
		
		//SE ORDENA DE MAYOR A MENOR, CON UN COLUMNSORT PARA REPETIR LOS PASOS EN AMBAS LISTAS
		
		columna=bubbleSort(size, columna);
		//LA IDEA ES TENER UNA LISTA CON LAS COLUMNAS ORGANIZADAS POR TAMAÑO, DE MAYOR A MENOR
		 //PARA ESTO ULTIMO, SE INVIERTE LA LISTA COLUMNAS
		int aux;
		Collections.reverse(columna);
		 
		 //System.out.println(columna);
		 
		 //UNA VEZ OBTENIDAS LAS COLUMNAS POR ORDEN DE TAMAÑO (MENOR A MAYOR) SE RELLENAN
		 int c_d;
		 int rCol;
		 for (i=0;i<columna.size();i++)
		 {
			 //SE OBTIENE LA COLUMNA
			 c_d=(int)columna.get(i);
			 temp=(Column)field.get(c_d);
			 
			 //SI LA ALTURA ES MENOR A HMAX Y ESTA ORDENADO
			 if (temp.size()<hMax && temp.isLock())
			 {
				 /**SE CREA UN RANDOM DE LAS POSIBLES COLUMNAS DE LA MISMA ALTURA**/ 
				 c_d=randomCol(temp.size(),columna);
				 
				 temp=(Column)field.get(c_d);
				 
				 
				 
				 aux=columnBestFill(c_d);
				 //EN CASO DE QUE SE PUEDA REALIZAR UN MOVIMIENTO
				 if (aux!=-1)
				 {
					 //REALIZA EL MOVIMIENTO Y VUELVE A INTENTAR CON LA PRIMERA COLUMNA
					 atomic_Move(aux, c_d);
					 i=0;
				 }
			 }
		 }
	}
	
	/**
	 * ELIGE COLUMNAS RANDOM ENTRE LA MISMA ALTURA
	 * @param h			altura maxima
	 * @param col		columnas en orden por tamaño
	 * @return			columna aleatoria con la que proseguir
	 */
	public  int randomCol(int h, ArrayList col)
	{
		int i;
		Column temp;
		ArrayList posibles=new ArrayList();
		int c;
		for (i=0;i<field.size();i++)
		{
			c=(int)col.get(i);
			temp=(Column)field.get(c);
			
			if (temp.size()==h)
			{
				if (temp.isLock() && temp.size()<hMax)
				{
					//System.out.println(temp+"  "+c);
					posibles.add(c);
				}
			}
			
		}
		
		
		int r=random(posibles.size());
		
		return (int)posibles.get(r);
		
	}
	
	/**
	 * DESATASCA EL PROGRAMA
	 */
	public  void RAIOVacateColumn()
	{
		Column temp;
		int i;

		//int col=selectColToVacate();
		
		//int col=select_column();**
		
		int col=selectMinumColR();
		temp=(Column)field.get(col);
		int selector;
		
		int promedio=totalContenedores()/field.size()+random(2);
		
		
		while (temp.size()!=0)
		{
			
			//PRIMERO SE VE SI PUEDE UBICARSE EN OTRO LUGAR AHORRANDO MOVIMIENTOS
			selector=posibleMove((int)temp.get(temp.size()-1),col);
			if (selector!=-1)
			{
				atomic_Move(col,selector);
				
				if(temp.isLock() && !temp.isEmpty())
				{
					if (Contador(col)+temp.size()>=promedio)
					{
						break;
					}
				}
			}
			else
			{
				//EN CASO DE QUE NO PUEDA ENVIARSE A UN LUGAR DONDE SE AHORRE MOVIMIENTO SE MUEVE A UN LUGAR CONVENIENTE
				selector=bestVacateC_d(col);
				if (selector!=-1)
				{
					atomic_Move(col,selector);
					
					if(temp.isLock() && !temp.isEmpty())
					{
						if (Contador(col)+temp.size()>=promedio)
						{
							break;
						}
					}
				}
				//SI NO PUEDE ENVIARSE A UN LUGAR CONVENIENTE, SE REALIZA UN MOVIMIENTO ERRONEO A PROPOSITO
				else
				{
					selector=badMove(col);
					
					atomic_Move(col,selector);
					
					if(temp.isLock() && !temp.isEmpty())
					{
						if (Contador(col)+temp.size()>=promedio)
						{
							break;
						}
					}
					
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @return	COLUMNA MAS PEQUEÑA RANDOM.
	 */
	public  int selectMinumColR()
	{
		ArrayList size=new ArrayList();
		ArrayList columna=new ArrayList();
		int i;
		int aux;
		Column temp;
		//SE LEE EL TAMAÑO Y LA COLUMNA 
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			size.add(temp.size());
			columna.add(i);
		}
		
		//SE ORDENA DE MAYOR A MENOR, CON UN COLUMNSORT PARA REPETIR LOS PASOS EN AMBAS LISTAS
		
		ArrayList col=bubbleSort(size, columna);
		 
		columna=col;
		
		int c;
		
		ArrayList posibles=new ArrayList();
		c=(int)columna.get(0);
		temp=(Column)field.get(c);
		int h=temp.size();
		
		for (i=0;i<columna.size();i++)
		{
			c=(int)columna.get(i);
			temp=(Column)field.get(c);
			
			if (temp.size()==h)
			{
				posibles.add(c);
			}
			else
			{
				break;
			}
		}
		
		int r=random(posibles.size());
		return (int)posibles.get(r);
		
		
	}

	public  void writeBase(String s) throws IOException
	{
		FileWriter file = new FileWriter(s);
		PrintWriter pw = new PrintWriter(file);
		/*
		 * FILAS	COLUMNAS
		 * 
		 * TAMAÑO_COLUMNA N N1 N2 N3......
		 * TAMAÑO_COLUMNA N N1 N2 N3......
		 */
		
		pw.println(field.size()+" "+totalContenedores());
		
		int i,j;
		Column temp;
		
		for (i=0;i<field.size();i++)
		{
			temp=(Column)field.get(i);
			//CANTIDAD DE ELEMENTOS EN LA COLUMNA
			pw.print(temp.size()+" ");
			
			//ELEMENTOS EN LA COLUMNA
			for (j=0;j<temp.size();j++)
			{
				pw.print(temp.get(j)+" ");
			}
			pw.println();
		}
		
		file.close();
	}
	/**************************************************/
	//FUNCIONES PARA ACCEDER DESDE EL NODO.
	/**************************************************/	
	public int size()
	{
		return field.size();
	}

	public Object get(int i)
	{
		return field.get(i);
	}
	
	/**
	 * FUNCION QUE RESUELVE CADA NODO ENTREGANDO LA CANTIDAD DE MOVIMIENTOS QUE DEMORA
	 * @return				CANTIDAD DE MOVIMIENTOS
	 * @throws IOException
	 */
		public  int solve() throws IOException 
		{
			
			//CREACION DE COPIA DE LA INSTANCIA PARA NO VER AFECTADA LA ORIGINAL
			File dir=new File("src\\extras\\file2");
			writeBase("src\\extras\\file2");
			Field s=new Field();
			//LEE EL ARCHIVO
			s.readFile(dir);
			//SE ACTUALIZA EL CAMPO
			s.review();
			//MOVIMIENTOS EN 0
			s.setMovs(0);
			//SE ORDENA
			while (!s.isOrdenado())
			{
				while (s.isFillPosible())
				{
					s.RAIOfill();
					//System.out.println();
					//s.showTime();
					//System.out.println("Movimientos hasta ahora: "+s.getMovs());
				}
				if (s.isOrdenado())
				{
					//System.out.println();
					//s.printMovs();
					break;
					
				}
				
				//Si no esta ordenado
				s.RAIOVacateColumn();
			}
			//RETORNA LA CANTIDAD DE MOVIMIENTOS QUE TOMA (PUNTAJE)
			return s.getMovs();
		}

	
}
	
	
	
	

	
	
/*
 * 
 * 
	                                                                           `-:-.`                                                                                                                  
                                                                         .:++++++/-`                                                                                                               
                                                                       ./+++/:-:/+o+:`                                                                                                             
                                                                     `:+o+/:-----::+oo-`                                                                                                           
                                                                   `:+oo+:---------:/+o+-                                                                                                          
                                                                  -+oo+/-------::::--:/oo/.         -/+/:.                                                                                         
                                           `..`                 `:ooo+:::::--:/+++++/--:+oo:`     ./o+/+++:`                                                                                       
                                         -/+++o++:.`           ./ooo+:----::-/+++/.-+/---:+o+-`  .os/:::/+++-                                                                                      
                                        :+++///+++o+/:.`      .+ooo+::------:++++/``:/----:/+o/`.+o/::::::/++:`                                                                                    
                                       :+o+/::::::///+++:-`  ./+oo+:---------/+++++/+/------:+o++o/::::::::/++-                                                                                    
                                      .++++:::::::::--://+/:-/++++:----------:+++++++:-------:+oo+:--:::::::/++-                                                                                   
                                      :oo+/::::::::::---:::/++++/:------------:/+++/:---------:++/----:::::::+++.                                                                                  
                                     `+oo+/::::::::::-------://::---------------:::------------::-----:::::::/++/`                                                                                 
                                     -ooo+/:::::::::::---------------------------::////////::----------:::::::+o+.                                                                                 
                                     -ooo+/:::::::::::----------------------:/osyhhhhhhhhhhhhyys+/:----:::::::/oo/                                                                                 
                                     :oooo/::::::::::::::/++++o+++///:--:/+yhhhhyso/::----:/+osyyhyo/:--::/:::/oso`                                                                                
                                     :oooo/::::::::/+oyyhhhdddddddhhhysoshdhy+:.```           ``.:oyhs+ssyyyyysyhs-`                                                                               
                                     .osso/::::::+shhdhhso/::-----:/+syhdhs:``                    ``--////////+syhhyo/`                                                                            
                                     `osss+::::+yhdhy+-.``          ```-/:.                   ```..-----.````````.-+shyo-                                                                          
                                     `/ssss:::+hddy:``   `.--:::::-..````````````````````` ``.:oyyhhhhhhhyo+:.```````-oyho-                                                                        
                                      -syyysyyhddy-   `-+syhhhhhhhhhyso/:.`````````````````-+yhdhysoooosyhddhhs+-.`````.ohh:                                                                       
                                   ./syhhys+//+yy+.`-+yhhhhddddddddddhdhhyo/-.```````````.+ydhs+:--------:/oyhdhhs+-.````:yh+`               :.                                                    
                                 .+hhhyo:.`````....+yhddddhysooooosyyhhddddhyo:.`````````+hho/--------------:/oyhddyo:.```-yh/              `s-                                                    
                                /ydhs:.``````````:shddddhs+:---------/+syhddddhy/:::::::/oso+////:-------------:/oyddhs:.``:hh.              .`                                                    
                               +hdh/.` ````````./hddddhy+:--------------:/oyhdddhhhhhhhhhhhhhhhhhhhys/--------:::::+yhdhs:.-yd-                                                                    
                              `ydd+` `````````-ohddddhs:::-:----------------/oyyyyssooo++/////////+o+:-----::::::::::+yhdho:yh.                                                                    
                              `ohy:``...`````.sdddddhs::::::::-----------------------------------------:--:::::::::::::ohddhho                                                                     
                               .+sssyyyys+/.`-hdddddy/:::::::::://:------------------------------------:::::::::::+/::::/shdds`                                                                    
                              `+hddddddddhy/-:oyyyyo/::::::::::+hho:------------------------------------:::::::::shh+::::/ohdds-                                                                   
                              -hdddhysoooo+:::::///////::::::::sddy/:------------------------------------:-::::::yddo::::+syhhdh/               `:`                                                
                              :hdddho//////:::/+ossssssso::::::oddh/::-----------------/o/----------:oo:-::::::::+yy+::::+sssyhdh+.              `.                                                
                              -yddddy//////:::+ssssssssss/::::::+o+:::::::-------------/sy/---------:o+::-::::::::::::::://ossyyhhs-                                                               
                           `-/shddddy///////::/+ossooo+//:::::::::::::::::::------------::--------------::::::::::::::::::///+++sddy-                                                              
                          -ohdddddhyo///////::::::::::::::::::::::::::::::::::::-----------------------::::::::::::::::::////////shdy-                                                             
                         `shddddhs+//////////::::::::::::---::::::::::::::::-::----:::://+++oooossoo+//::::::::::::::::::////////+hdh+                                                             
                         .yhddddhs////////////::::::::::::::::::::::::::::::://+oossyyhhhhhhhyyssssyhhhyyso+/:::::::::::::://////+hdh:                                                             
                         `shdddddhs///////////::::::::::::-::::::::///+ossyyyyyyyysso+/::----...``..--:/osyyyyyso+//::::::://////shdy.                                                             
                          -yddddddho//////////::::::::::::::/++osyyhhhyyso++/:--...``````````````````````..--/+osyhhyo::::://///ohdd+                                                              
                           -yddddhs//////////:::::://++ossyyyyyso+/::-....`````````````````````````````````````...-:++-..-::://ohddy`                                                              
                            ./+o++/////::::::::::::+ssso+//:---......`````````````````````````````````````````````````````..-:ohddh-                                                               
                                 -oss+:------........................................```````````````````````````````````````.+hddy:                                                                
                                 /hddh/-..................................................`````````````````````````````````-shdhy-                     ``                                          
                                -ydddy-.........................................................`````````````````````````./yhdhs-                                                                  
                               `shddd+........................................................................`````````.:shddh/`                                                                   
                               `hddddyo/////::--................................................................`````.:shddhs-                                                                     
                              `-sddddddddddddhhyso+:--..........................................................``../shddhy/`                                                                      
                            .+shdddhhyysssyyyyhhhdddys/--........................................................-+yhdddy/.                                                                        
                          .ohddhs+:.```` `````.--/+syhdho//++oo+/:--:////::--........................----..:/ossyhhhhddh+-`                                                                        
                         -hddy/.`````````````      ``-/sys++/++osyyyyysssyyyys+/-.................-/oyyysoshhs+/----::+oyhy+-`     `...                                                            
                         oddh-```````````````````      `.`     ``./:.`````.-:+syy+-...---....--:::syo/-...-:-`        ``.:+yhs-  `:shhys/`                                                         
                         oddh:.`````````````````````                    `     `.+yy:/+ooo/:-/syyyys:`            ``````````-+yh/`+hdo/odds.                                                        
                         :ddds-.-://++oo+:.````````````````    ``````````````````/hhhhyyyssyhy/....````      `````````.::..../hhsddo---sdds`                                                       
                        ``/ysoshhddddddddhs/.```````````````````````````````````.shhso++/..+s-````````````````````````-shsyyyhdddho:---+hdh/                                                       
                      .oysosyddddhhhyyhhddddyo/-..``````````````.-:-..`````````./hdyo++++++++/.`````````````````````.:ydyyssoooo+/--:::/hddo`                                                      
                     :yddysshdddy/:::::/shhddddhysoo++////+/::/+shdhyso+/:::::/ohddho+++++++yds-.......-//-.`````..:ohho------:::::::::+hddo`                                                      
                    /hhhyssshdddh/::::::::/+oyhhhdddddddddddhhyo+/osyhhhhhhhhhhysyddhyssossydddhhyyyyyhhhdhs+///+syhyo:----::://////:/+hddh:                                                       
                   :hhhyssssydddds::::::---.`.-://+oossssoo+/-..```..-::////::-..-+yhhdhhhhyo/++ooooo++/:+yhhhhyys+:-.--:::///+++oosyhdddy:                                                        
                  :yhhhsssssshdddh+::::::--``````````````````````.`.........`````..-::///:-.``````````````..--...`````-/syyhhhhddddddddh+.                                                         
                 -yhhhyssssssyddddo::::::-.``````````.``````..------------------/syyyo/-.`````````````````````````````:hddddddddddddddy`                                                           
                 shhhyssssyyhddddy/::::::-.`````````````````.-::::::------------shhhddddy/.```````````````````````````./hddddhhhyyyyhhy.                                                           
               `/hhhysssyhdddddy+/::::::-.`````````````````.-::::::::::::::::-----::/+yddds-```````````````````````````.odddhdhhyssssyyo.                                                          
               -yhhhssshddddhs+//:::::::-``````...````````.+yhyo/::::::::::::::::::::-:+ddds````````````````````````````-yddyoyddhsssyyy/`                                                         
              `ohhhysshdddhs+///::::::::.`````........````.odddddhyso+++//////:::::::/+ydddy````````````````````````````./hdds:oddysssyyy-                                                         
              :yhhyssydddds////::::::::-.```............```.+hdddddddddddddhhhysssyyhdddddd/```````````````````````......-oddh+ydhsssssyyo`                                                        
              +hhhyssyddddy+///::::::::-.``..............````.:+oyhdddddddddddddddddddhyosdy.````````````````````.........:hddhdhssssssyyy:                                                        
              +hhyssssyddddy+//::::::::-``................````````.-:/yddyyyyyyssoo+/::--/hd+```````````````````......-----oddddsssssssyyyo`                                                       
              +hhysssssyddddds+::::::::..........-..........````.````.odho++///:::::://+osddo.``````````````````...--------:hddddyssssssyyy:                                                       
              +hhsssssssyhdddds:::::::-.........--................```.+dds+ossssosssssyyhhdy-``````````````````...----------yddhhhysssssyyy+`                                                      
              +hysssssssssydddy/::::::-.....--........................-sdddhhhy+/shdddddhy+-..........```.........-.--------sddyohdhsssssyyy.                                                      
              +hsssssssssydddds/::::::-.......-.........................:+syyddo:/syds+:--......................------------sddy/ohdyssssyyy/                                                      
              +yssssssssyddddy+/:::::::-.......-............................-hdy:--yd+..............................-------/yddyshdhssssssyyo`                                                     
              +ysssssssshddddo//:::::---.........-..........................-hdy:--yd+.............................---..--:ydddddhysssssssyys.                                                     
              +ssssssssyddddh+///:::::--.........-..........................:hdy:--ydo...............................----+yddddhhsssssssssyyy-                                                     
              /ssssssssyhddddyo++/:::::-..................................../ddh++ohds...............................-:+yhddhhyyssssssssssyyy/                                                     
              /sssssssssyddddddddhy+::::-........-............-....--......./ddho++sdy..--.....-...................-/ohddddhysssssssssssssyyy+      
 */
	
	

