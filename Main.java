import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.io.InputStreamReader;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Main {
	
	static int size;
	//static ArrayList<ArrayList<Integer>> poblacion = new ArrayList<ArrayList<Integer>>();

	
	
	//POBLACION INICIAL USANDO DFS
	public static ArrayList<ArrayList<Integer>> dfs(int first, int last, int [][] mat, boolean[] flag, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> poblacion) {
	    
		
		ArrayList<Integer> aux = new ArrayList<Integer>();
		//nodo inicial se agrega
		
        list.add(first); //nodo inicial se agrega
        size++;
        //imprimeMat(mat);
        
        
        
        flag[first] = true; //marcar nodo como visitado
        
        if (first == last) { 
        	
            for (Integer node : list) {
                aux.add(node); //agregar nodo a lista auxiliar
                
                
                //path.add(node);
                
            }
           
            poblacion.add(aux); //agregar lista auxiliar a poblacion
         
        }
        
        
        for (int I = 1; I <= last; I++) {
       
        //si no hay vertices entre first y el nodo actual
            if (mat[first][I] == 1) {
				
                if (flag[I] == false) { //si el nodo actual no se ha visitado
					
                	//Recursividad sobre nodo actual
                    dfs(I, last,mat,flag,list,poblacion);
                    
                    
                    //Marcar el nodo actual como no visitado para buscar otro camino al destino
                    flag[I] = false;
                    
                    
                    //el tamaño de la lista se reduce 1 mientras el nodo se marca como no visitado
                    size--;
                    
                    //eliminar el nodo en la posicion (size)
                    list.remove(size);
                }
            }
        }
    
        return poblacion;
    }

	

	 private static void invierteMat(int[][] mat) { 
	        for(int i=0;i<mat.length/2;i++) { 
	            for(int j=0;j<mat.length;j++) { 
	                int temp = mat[i][j]; 
	                mat[i][j] = mat[mat.length-i-1][mat.length-j-1]; 
	                mat[mat.length-i-1][mat.length-j-1] = temp; 
	            } 
	        } 
	    } 
	public static void imprimeMat(int[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			  
            // Loop through all elements of current row 
            for (int j = 0; j < mat[i].length; j++) { 
                System.out.print(mat[i][j] + " "); 
            }
            System.out.println();
		}
		
    } 
	/*
	public static void matSearch(LinkedList<Actividad> cam, int[][] mat) {
		
		for (int i = 0; i <=cam.size(); i++) {
			//System.out.println(cam.get(i).getId());

			if(cam.get(i+1).dependencias.contains(cam.get(i).getId())) {
				for (int j = 0; j < cam.get(i+1).dependencias.size(); j++) {
					//System.out.println();
					mat[i+1][cam.get(i+1).dependencias.get(j)] = 1;
					System.out.println(cam.get(i+1).dependencias.get(j));
					
				}
				//System.out.println("EXITO -Dependencia en la actividad: "+cam.get(i+1).getId());
				
			}
		}
	}
*/
	
	public static int tiempoTotal(LinkedList<Actividad> camino){
		int total=0;
		for (int i = 0; i < camino.size(); i++) {
			total += camino.get(i).tiempo;
		}
		
		return total;
	}
	
	/*
	public static int funcionAptitud(LinkedList<Actividad> cam) {
		int puntos=0;
		
		puntos+=tiempoTotal(cam);
		
		for (int i = 0; i < cam.size()-1; i++) {
			
			if(cam.get(i+1).dependencias.contains(cam.get(i).getId())) {
				System.out.println("EXITO -Dependencia en la actividad: "+cam.get(i+1).getId());
				puntos +=100;
			}else {
				puntos+=50;
			}
			
		}
		if(cam.getFirst().getId()==0){
			System.out.println("EXITO - Primer nodo ");
			puntos += 200;
		}if (cam.getLast().getId() == cam.size()-1) {
			System.out.println("EXITO - Último nodo ");
			puntos +=200;
		}
		return puntos;
		
	}
	*/
	
	
	//FUNCION DE APTITUD
	public static int fA(ArrayList<Integer> camino, Actividad act[] ) {
		int puntos=0;
		for (int i = 0; i < camino.size(); i++) {
			puntos+= act[camino.get(i)].getTiempo();
			//System.out.println(puntos);
		}
		return puntos;
	}
	
	//SELECCION (ELITISMO)
	public static ArrayList<ArrayList<Integer>>  seleccion (ArrayList<Integer> puntos,ArrayList<ArrayList<Integer>> poblacion ) {
		
		int s1 = Integer.MIN_VALUE, s2 = Integer.MIN_VALUE;
		ArrayList<ArrayList<Integer>> sobrevivientes = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < puntos.size(); i++) {
			if(puntos.get(i)>s1) {
				s2 = s1;
				s1 =i;
				
			}else if(puntos.get(i)>s2) {
				s2 = i;
			}
			
		}
		System.out.println(s1);
		sobrevivientes.add(poblacion.get(s1));
		sobrevivientes.add(poblacion.get(s2));
		
		
		return sobrevivientes;
	}
	
	public static ArrayList<ArrayList<Integer>>  seleccion2 (ArrayList<Integer> puntos,ArrayList<ArrayList<Integer>> poblacion ) {
		
		int res=0,max=0;
		ArrayList<ArrayList<Integer>> sobrevivientes = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < puntos.size(); i++) {
			if(puntos.get(i)>max) {
				res =i;
				max = puntos.get(i);
				
			}
			
		}
		//System.out.println(res);
		System.out.println("DURACIÓN MÍNIMA: "+puntos.get(res)+" días");
		sobrevivientes.add(poblacion.get(res));
		//sobrevivientes.add(poblacion.get(res));
		
		
		return sobrevivientes;
	}
	
	//CRUCE (SINGLE POINT CROSSING)
	public static ArrayList<ArrayList<Integer>> cruce(ArrayList<ArrayList<Integer>>  sobrevivientes) {
		Random r = new Random();
		ArrayList<ArrayList<Integer>> nuevaPoblacion = new ArrayList<ArrayList<Integer>>();
		Object[] padre = sobrevivientes.get(0).toArray();
		Object[] madre = sobrevivientes.get(1).toArray();
		int puntoCruce = r.nextInt()*padre.length;
		//SINGLE POINT CROSSING
		for (int i = 0; i < padre.length; i++) {
			if(i<puntoCruce) {
				nuevaPoblacion.get(i).add(sobrevivientes.get(0).get(i));
			}
			else {
				nuevaPoblacion.get(i).add(sobrevivientes.get(1).get(i));
			}
		}
		
		return nuevaPoblacion;
	}
	

	public static void main(String[] args) throws IOException {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		String path;
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			
			File selectedFile = jfc.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
			path = selectedFile.getAbsolutePath();
		
	//String path = "/Users/paco/eclipse-workspace/Tesina/src/test4.csv";
	
	BufferedReader csvReader = new BufferedReader(new FileReader (path));
	BufferedReader csvReader2 = new BufferedReader(new FileReader (path));
	String row,row2;
	
	int n = 0;
	csvReader.readLine();
	csvReader2.readLine();
	long tiempoStart = System.nanoTime(); 
	
	while((row = csvReader.readLine()) !=null) {
		
		//data = row.split(",");
		n++;
		//System.out.println(data[1]);
		
	}
	
	Actividad act[] = new Actividad [n+1];
	int mat[][] = new int[n*2][n*2]; 
	int mat2[][] = new int[n*2][n*2];
	int matDep[][] = new int[n*2][n*2]; 
	int matPrueba[][] = new int[8][8];  
	boolean[] flag = new boolean[n + 1];
	Arrays.fill(flag, false);
	
	LinkedList<Actividad> camino = new LinkedList<Actividad>(); 

	int id;
	int duracion[] = new int[n];
	int tiempo;
	
	//String descripcion [] = new String[count];
	
	for (int i=1; i <=n; i++) {
		String descripcion;
		LinkedList<Integer> dependencias = new LinkedList<Integer>();
		String [] data;
		String [] aux;
		
		row2 = csvReader2.readLine();
		data = row2.split(",");
		id = Integer.parseInt(data[0]);
		descripcion = data[1];
		tiempo = Integer.parseInt(data[2]);
		
		if(data[3].equals("0")==false){
			if(data[3].contains(" ")) {
				aux = data[3].split(" ");
				for (int j = 0; j < aux.length; j++) {
					dependencias.add(Integer.parseInt(aux[j].toString()));
					mat[i+(n-2)][(Integer.parseInt(aux[j].toString()))+(n-2)] = 1;
					
				}
				
			}else {
				dependencias.add(Integer.parseInt(data[3].toString()));
				mat[i+(n-2)][(Integer.parseInt(data[3].toString()))+(n-2)] = 1;
				
			}
			
		}
		
		
		
		
		
		act[i] = new Actividad(id,tiempo,dependencias,descripcion);
		camino.add(act[i]);
		//duracion[i] = Integer.parseInt(data[2]);
		System.out.println("\nActividad: "+act[i].getId());
		System.out.println("Descripcion: "+act[i].descripcion);
		System.out.println("Tiempo: "+act[i].getTiempo());
		System.out.println("Dependencia: "+act[i].dependencias);
		
			
		}
	System.out.println("\nNúmero de actividades: "+n);
	csvReader.close();
	System.out.println("-----------------------------------------------");

	System.out.println("MATRIZ DE DEPENDENCIAS");
	//imprimeMat(mat2);
	System.out.println("-----------------------------------------------");
	invierteMat(mat);
	imprimeMat(mat);

	//matSearch(camino,mat2);

	/*
	matPrueba[1][2] = 1;
	matPrueba[1][3] = 1;
	matPrueba[2][4] = 1;
	matPrueba[3][4] = 1;
	System.out.println("-----------------------------------------------");
	imprimeMat(matPrueba);
	//invierteMat(matPrueba);
	
	*/
	
	ArrayList<ArrayList<Integer>> poblacion = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> list = new ArrayList<Integer>();
	ArrayList<Integer> individuo = new ArrayList<Integer>();
	ArrayList<Integer> puntos = new ArrayList<Integer>();
	ArrayList<ArrayList<Integer>>  sobrevivientes = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> nuevaPoblacion = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> nuevoCamino = new ArrayList<Integer>();
	int contador=5;
	
	
	//POBLACION INICIAL
	
	poblacion = dfs(1,camino.getLast().id,mat,flag,list,poblacion);
	System.out.println("-----------------------------------------------");
	System.out.println("POSIBLES CAMINOS:");
	System.out.println("-----------------------------------------------");

    for (int i = 0; i < poblacion.size(); i++) { 
    	
        for (int j = 0; j < poblacion.get(i).size(); j++) { 
            System.out.print(poblacion.get(i).get(j) + "->"); 
        } 
        System.out.println(); 
    }
    
    //FUNCION DE APTITUD
    for (int i = 0; i < poblacion.size(); i++) {
    	individuo = poblacion.get(i);
    	puntos.add(fA(individuo,act));
    	
	}
   
	System.out.println("\n-----------------------------------------------");
	System.out.println("PUNTUACIONES DE INDIVIDUOS");
	System.out.println("-----------------------------------------------");

    for (int i = 0; i < puntos.size(); i++) {
	   System.out.println("PUNTOS DE INDIVIDUO: "+(i+1)+" -> "+puntos.get(i));
}
    long tiempoEnd = System.nanoTime(); //END TIMING 1
    long tiempoTotal = (tiempoEnd - tiempoStart); //TIMING 1 DURATION
    double segundos = (double)tiempoTotal / 1_000_000_000.0;
    
	System.out.println("\n-----------------------------------------------");
	System.out.println("RESULTADOS");
	System.out.println("-----------------------------------------------");
    //SELECCION
    sobrevivientes = seleccion2(puntos,poblacion);
    //sobrevivientes = seleccion(puntos,poblacion);
    puntos.clear(); //Vaciar puntos
    
    System.out.print("CAMINO CRÍTICO: ");
    for (int i = 0; i < sobrevivientes.size(); i++) { 
    	
        for (int j = 0; j < sobrevivientes.get(i).size(); j++) {
        	if(j==sobrevivientes.get(i).size()-1) {
        		System.out.print(sobrevivientes.get(i).get(j) + " "); 
        	}else {
        		System.out.print(sobrevivientes.get(i).get(j) + "->"); 
        	}
            
        } 
        System.out.println(); 
    }
   
    System.out.println("\nACTIVIDADES CRÍTICAS (ID):");
    for (int i = 0; i < sobrevivientes.size(); i++) { 
    	
        for (int j = 0; j < sobrevivientes.get(i).size(); j++) {
        	if(j==sobrevivientes.get(i).size()-1) {
        		System.out.print(sobrevivientes.get(i).get(j) + " "); 
        	}else {
        		System.out.print(sobrevivientes.get(i).get(j) + ","); 
        	}
            
        } 
        System.out.println(); 
    }
    
    
    
    System.out.println("---------------------------------------------------");
    System.out.println("ACTIVIDADES CRÍTICAS: \n");
    for (int i = 0; i < sobrevivientes.size(); i++) { 
    	
        for (int j = 0; j < sobrevivientes.get(i).size(); j++) {
        	if(j==sobrevivientes.get(i).size()-1) {
        		System.out.print((j+1)+".- "+act[sobrevivientes.get(i).get(j)].descripcion); 
        	}else {
        		System.out.print((j+1)+".- "+act[sobrevivientes.get(i).get(j)].descripcion+"\n"); 
        	}
            
        } 
        System.out.println(); 
    }


    nuevaPoblacion = sobrevivientes;
    
    //FUNCION DE APTITUD
    for (int i = 0; i < nuevaPoblacion.size(); i++) {
    	nuevoCamino = nuevaPoblacion.get(i);
    	puntos.add(fA(nuevoCamino,act));
    	
	}
    poblacion.clear();
    individuo.clear();
    puntos.clear();
    sobrevivientes.clear();
    
    poblacion = nuevaPoblacion;
    nuevaPoblacion.clear();
    nuevoCamino.clear();
    System.out.println("\n---------------------------------------------------");
    System.out.println("Tiempo de ejecución: " + segundos +" segundos");

    
		}	
	}
}
