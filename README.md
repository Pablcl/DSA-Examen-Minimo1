# DSA-Examen-Minimo1

Examen del Minimo 1 de la asignatura de DSA que consiste en un proyecto de Gestión de Libros.

-Al acabar el examen en clase ya funcionaban las funciones correctamente.

-Los bugs o vulnerabilidades detectados posteriormente se han corregido.

-Se ha cambiado la lista de pilas por una cola de pilas mejorando las estructuras de datos.

-He decidido mantener las listas y no implementar hashmaps debido a que el enunciado no lo requería explícitamente, las búsquedas actuales no son costosas en rendimiento. Además, las operaciones que se realizan son de tamaño limitado y no justifican el uso de una estructura más compleja.

-Se ha corregido el metodo catalogarLlibre ya que me estaba guiando por el id del libro y no por el isbn para hacer la comprobación de encontrar un libro igual.

-El atributo "estat" de prestec se ha eliminado del constructor de prestec evitando asi que el valor introducido por el usuario se procese.
He buscado info por internet, para añadir una funcionalidad que haga que swagger no pida el estado del prestamo al usuario (lo oculte) ya que no tiene sentido que se le pregunte, y una vez el usuario añada el prestamo si que se actualize a "en tramite" pero finalmente he decidido no añadirla.

-Se han añadido excepciones tipo not found para mejorar el control de errores.
