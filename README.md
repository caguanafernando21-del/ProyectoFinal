# Universidad Politécnica Salesiana 
## Ingeniería en Ciencias de la Computación

### **Asignatura**: Estructura de datos

### **Tema**: Proyecto Final

### **Título**: Implementación y Visualización de rutas en un mapa de calles mediante **BFS** y **DFS** 

### **Fecha**: Cuenca, 28 de julio 2026

### Integrantes
 - Angelo Carchipulla ( acarchipullap@est.ups.edu.ec )
 - Fernando Caguana ( lcaguanas1@est.ups.edu.ec )
- José Astudillo ( jastudillop6@est.ups.edu.ec )
## Indice de contenidos
#### 1. Objetivos
#### 2. Descripción del problema
#### 3. Marco teórico
- 3.1 Grafos y teoría de grafos

- 3.2 Algoritmos de recorrido y busqueda

  - 3.2.1 Breadth-First-Search (**BFS**)

  - 3.2.2 Depth-First-Search (**DFS**)

  - 3.2.3 Complejidad computacional
#### 4. Tecnologías utilizadas 
#### 5. Diagrama UML
#### 6. Arquitectura y estructura de carpetas
 - 6.1 Arquitectura por capas

 - 6.2 Modelo-Vista-Controlador (**MVC**)

#### 7. Explicación general del funcionamiento
#### 8. Capturas de configuraciones
#### 9. Algoritmos
- 9.1 Lógica de Breadth-First-Search

- 9.2 Lógica de Depth-First-Search
#### 10. Tabla comparativa de Resultados

#### 11. Conclusiones
#### 12. Recomendaciones y posibles aplicaciones futuras.

##
## 1. Objetivos
General: Desarrollar un sistema en Java que permita modelar un mapa de referencia real basado en una zona geográfica específica obtenido de la aplicación **Google Maps**, representándolo como una estructura de datos no lineal denominada como *"Grafo"*, compuesta por nodos y aristas.

Específico 1: Aplicar las arquitecturas de software aprendidas, basadas en la separación de responsabilidades, usando patrones como Modelo-Vista-Controlador (**MVC**) para estructurar los componentes

Específico 2: Evaluar el comportamiento de los algoritmos **BFS** y **DFS** empleando pruebas experimentales y principios del Benchmarking.
##
## 2. Descripción del problema
Dado el contexto de las múltiples conexiones entre los mapas del mundo real: rutas, intersecciones, vías unidireccionales e incluso túneles. Resulta útil pensar en puntos conectados entre sí, similar a una red altamente conectada.

Es por ello que, las estructuras computacionales como los grafos resultan útiles para modelar fácilmente estas conecciones de forma explícita mediante nodos (**puntos**) y aristas (**conexiones**). De manera que representar a una ruta como un grafo, permite que un algoritmo realice operaciones como: recorre ubicaciones, recorrer nodos y buscar un camino adecuado hacia un punto de interés. 

Se pueden utilizar otras estructuras. Sin embargo, los grafos representan relaciones explícitas.
##
## 3. Marco teórico
### 3.1 Grafos y teoría de grafos
Se trata de una estructura de datos no lineal la cual permite representar un grupo de entidades. Formalmente se trata de una estructura matemática designada por ***G = (V, A)***, donde según Álvarez y Parra (2013), V resulta ser un conjunto de puntos y A es un grupo de líneas (aristas) que unen dos puntos

Al plasmar un mapa a través de un grafo, se puede modelar las conexiones existentes entre diversas ubicaciones, de manera que se pueda analizar los distintos tipos de recorridos entre dos nodos ***A*** y ***B***.

**Figura 1**

*Ejemplo de grafo*

![alt text](<WhatsApp Image 2026-07-28 at 10.58.41 PM.jpeg>)

*Nota.* Adaptado de *"Taller - Grafos"*, por Sistema Uniremington, s.f., Sistemas Uniremington https://sistemasuniremingtonb.webnode.com.co/taller-grafos/


### 3.2 Algoritmos de recorrido y búsqueda
Una vez representado un problema mediante  un grafo, existen diversas formas de explorar y buscar sus vértices para analizar las relaciones entre ellos. Como no se trata de un camino lineal, naturalmente se emplean varios algoritmos, de los cuales dos tendrán un enfoque total en este proyecto.

 - **Deepth-First-Search (DFS)** 
 - Dijkstra
 - A* (A-Star)
 - Topological Sort
 - **Breadth-First-Search(BFS)**
 - Prim

### 3.2.1 Breadth-First-Search (BFS)
Es igualmente llamado como búsqueda por anchura, se trata de un algoritmo empleado para explorar vértices de un grafo manteniendo un orden  definido por niveles, visitando primeramente los vértices que se encuentren más cerca de un nodo inicial para posteriormente recorrer aquellos encontrados a mayor distancia.

Posee una característica primordial, la cual se basa en la idea de no encontrar un camino más corto o con la menor cantidad de aristas entre un origen y un destino, no utiliza pesos. En nuestro proyecto, explorará calles de salen de una intersección inicial antes de avanzar a zonas alejadas.

**Figura 2**

*Recorrido BFS*

![alt text](<WhatsApp Image 2026-07-28 at 11.01.40 PM.jpeg>)

*Nota.* Adaptado de *"BFS Algorithm : Breadth First Search Algorithm Tutorial.* Simplilearn. https://www.simplilearn.com/tutorials/data-structure-tutorial/bfs-algorithm

### 3.2.2. Depth-First-Search (DFS)
Se trata de una búsqueda en profundidad, algoritmo empleado para la exploración de vértices en un grafo mediante una estrategia basada en explorar un camino profundamente antes de regresar y considerar otros caminos. Su diferencia con BFS reside en el nivel de avance en un camino, siendo máximo en el caso de DFS antes de retroceder, aplicando un retroceso o ***Backtracking***.

Se lo puede marcar como una serie de pasos:

1) Seleccionar un nodo de inicio
2) Marcarlo como nodo visitado
3) Seleccionar un nodo que no ha sido visitado
4) Repetir los pasos anteriores hasta encontrar un nodo sin vecinos 
5) Finalmente retroceder y continuar con otras ramas.

**Figura 3**

*Recorrido DFS*

![alt text](<WhatsApp Image 2026-07-28 at 11.09.01 PM.jpeg>)

Nota. De What is DFS (Depth First Search) Algorithm? [Explicado con Ejemplos], por upGrad, 2023, upGrad Blog (https://www.upgrad.com/blog/what-is-dfs-algorithm/). En el dominio público [o Reimpreso con permiso].

### 3.2.3. Complejidad computacional
A través de la complejidad se permite medir el rendimiento de un algoritmo mediante la cantidad de recursos que este requiere en función del tamaño de entrada de datos. Dentro del caso de grafos específicamente, se evalúa considerando la cantidad tanto de aristas y vértices que este posee.

**BFS** posee una complejidad temporal denotada por ***O(V + E)***. Ya que cada vértice es visitado una ocasión y cada arista se considera en el proceso de exploración de sus conexiones.

**DFS** presenta una complejidad similar, esto debido a que durante el recorrido cada nodo se marca en una sola ocasión y cada conexión evaluada durante la profundidad. Sin embargo, va a depender de la representación del grafo en
 - Lista de adyacencia (***O(V+E)***)
 - Matriz de adyacencia (***O(V²)***)

 En el caso de nuestro proyecto, este posee una implementación clásica del BFS, por lo tanto su complejidad es ***O(V+E)***, ya que cada vértice es procesado una sola vez en una Cola <code>Queue</code>

## 4. Tecnologías utilizadas

**Tabla 1**

*Tecnologías empleadas en nuestro proyecto*


|  Tecnología       | Uso dado |
|------------        |-------------|
| **Java 25**         | Lenguaje principal de desarrollo de nuestro proyecto.
| **Java Swing**      | Biblioteca empleada para la creación de la interfaz gráfica interactiva usando botones, menús, paneles, etc.
| **Java Timer**      | Librería empleada para controlar los tiempos de animaciones (aparición de nodos y ruta final) de ambas búsquedas al abrir el programa y ejecutarlo.
| **Gson2.10.1**      | Librería empleada principalmente para la lectura y escritura de archivos con formato <code>.json</code>
| **JSON**            | Formato que almacena la información del mapa en sus propios objetos clave-valor (nodos, conexiones y coordenadas)
| **Git-Github**      | Herramientas empleadas para el control de versiones del proyecto, así como de su almacenamiento mediante guardados.
| **Figma**          | Herramienta empleada para el diseño de la distribución del mapa. Igualmente de la ubicación y establecimiento de cada nodo.
| **Colecciones-java** | Uso de estructuras de datos como HashMaps, HashSet, LinkedHashMap, LinkedHashSet, Queue y List

*Nota.* Elaboración propia

## 5. Diagrama UML

**Figura 4**

*Diagrama de clases de nuestro proyecto*

![alt text](<WhatsApp Image 2026-07-28 at 11.03.02 PM.jpeg>)

*Nota.* Elaboración propia

## 6. Arquitectura y estructura de carpetas

### 6.1 Arquitectura por capas
Nuestro proyecto mantiene una organización por capas, más precisamente a una división lógica del programa donde cada grupo de clases posee una responsabilidad en específico. Ya que en lugar de agrupar los algoritmos de visualización, recorrido y almacenamiento, cada capa mantiene su enfoque en una tarea mientras puede comunicarse con otras al hacerlo.

- Capa de presentación (<code>MainFrame</code>, 
<code>MapPanel</code> )
- Capa de lógica y control (<code>MapController</code>, <code>BFSPathFinder</code>, <code>DFSPathFinder</code>)
- Capa de modelo (<code>MapPoint</code>, <code>Graph</code>,
<code>Node</code>, <code>PathResult</code>)
- Capa de persistencia (<code>GraphRepository</code>, <code>FileGraphRepository</code>)

### 6.2 Modelo-Vista-Controlador (MVC)
En este patrón arquitectónico de software, se separan el Modelo, la Vista y el Controlador con el objetivo de
realizar un acoplamiento entre la interfaz interactiva (GUI) incluyendo la lógica del sistema con su representación
de datos. De forma que cada componente pueda evolucionar de forma independiente.

Nuestro modelo se basa en las reglas principales del programa, depediente de una interfaz MainFrame, se encuentra
compuesto por <code>MapPoint</code> encargado de representar información relacionada con la ubicación del mapa, almacenando identificador y coordenadas de ubicación. Por otro lado se tiene a <code>Node</code> que representa los puntos donde pueden haber vías alternativas entre calles (izquierda, derecha, arriba o abajo), <code>Graph</code> el cual administra los nodos y <code>BFSPathFinder</code> junto con <code>DFSPathFinder</code> los cuales contienen la lógica de búsqueda de rutas

## 7. Explicación general del funcionamiento 
El funcionamiento del sistema empieza primeramente con la carga del mapa siendo almacenada en un archivo JSON. Mismo que posee toda la información de los nodos junto con sus conexiones entre estos. La clase FileGraphRepository es encargada de leer dicha informacion para crear la estructura del grafo dentro de la memoria.

Una vez cargado, cada ubicacion del mapa se representa visualmente con un MapPoint el cual se convierte en un nodo de Graph. El algoritmo recibe un grafo, nodo inicial y destino cualquiera, registrando los recorridos de acuerdo a cada uno de las búsquedas, empleando estructuras específicas como colas junto con estrategias recursivas.

## 8. Capturas de configuraciones 
Ejemplo configuracion incorrecta

![alt text](<WhatsApp Image 2026-07-28 at 11.03.28 PM.jpeg>)

Configuracion correcta 

![alt text](<WhatsApp Image 2026-07-28 at 11.03.45 PM.jpeg>)

## 9. Algoritmos

### 9.1 Lógica de Breadth-First-Search

Se realiza una búsqueda basada en explorar el grafo de acuerdo a sus niveles, comenzando desde un nodo inicial para visitar cada uno de los siguientes nodos encontrados a una distancia cercana antes de avanzar a niveles mas avanzados. Para lograrlo se utiliza una estructura de Queue (cola). Cada nodo registra su nodo padre para reconstruir la ruta donde se encuentra el camino
### 9.2 Lógica de Depth-First-Search

En este caso, el algoritmo realiza una búsqueda en profundidad siguiendo un camino desde el nodo inicial para llegar lo más lejos posible antes de seguir con un retroceso también llamado ***Backtracking*** para explorar otras alternativas. Su funcionamiento es basado en la recursividad, mediante la cual cada nodo visitado es responsable de llamar al algoritmo nuevamente

## 10. Tabla comparativa de Resultados
**Figura 4**

*Tabla comparativa de resultados obtenidos*

![alt text](<WhatsApp Image 2026-07-28 at 11.04.08 PM.jpeg>)

*Nota.* Elaboración propia. Los resultados son verificados en momento de ejecución en un computador específico

## 11. Conclusiones

Angelo Carchipulla: De acuerdo con en el desarrollo del proyecto, se puede observar la importancia de aplicar estructuras no lineales como en este caso los grafos y algorítmos de búsqueda como BFS y DFS, adentrándonos más a la resolución de problemas reales como la navegación y rutas.

Fernando Caguana: El uso de los nodos llega a ser algo muy complejo que a su ves es algo necesario ocupar para desarrollar buenos algoritmos, ya que gracias con esto logramos que el algoritmo sea eficiente y rapido.

José Astudillo: El desarollo de todo este sistema ha permitido implementar varias estructuras de datos junto con principios de Programación Orientada a Objetos (POO), todo ello mediante la implementación de un mapa como grafo, de manera que la implementación tanto de DFS como BFS proponen dos formas diferentes de resolver un mismo problema, cada búsqueda con su respectivo recorrido y lógica.

## 12. Recomendaciones y posibles aplicaciones futuras.

Como una recomendación final se considera primordial mejorar la representacion visual del grafo de manera que se lo pueda enfocar más a un sistema real: con pesos, restricciones. Puntos similares a un programa de rutas real. De igual manera es recomendable ampliar la persistencia, lo cual puede lograrse transportando datos de un archivo JSON hacia una base de datos (en el caso de que la información aumente)