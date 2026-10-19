
const API_USUARIOS = "/api/usuarios";
const API_LIBROS = "/api/libros";
const CLAVE_SESION = "usuarioActual";

let librosMostrados = [];


const vistaAuth = document.getElementById("vistaAuth");
const vistaApp = document.getElementById("vistaApp");

const loginForm = document.getElementById("loginForm");
const registroForm = document.getElementById("registroForm");
const msgLogin = document.getElementById("msgLogin");
const msgRegistro = document.getElementById("msgRegistro");

const nombreUsuario = document.getElementById("nombreUsuario");
const btnCerrarSesion = document.getElementById("btnCerrarSesion");

const tituloFormulario = document.getElementById("tituloFormulario");
const libroForm = document.getElementById("libroForm");
const libroId = document.getElementById("libroId");
const inputNombre = document.getElementById("nombre");
const inputAutor = document.getElementById("autor");
const inputGenero = document.getElementById("genero");
const inputAnio = document.getElementById("anioDeCreacion");
const inputUrlFoto = document.getElementById("urlFoto");
const btnGuardar = document.getElementById("btnGuardar");
const btnCancelar = document.getElementById("btnCancelar");
const msgLibro = document.getElementById("msgLibro");

const buscarForm = document.getElementById("buscarForm");
const buscarTexto = document.getElementById("buscarTexto");
const btnLimpiarBusqueda = document.getElementById("btnLimpiarBusqueda");
const msgLista = document.getElementById("msgLista");
const tablaLibros = document.getElementById("tablaLibros");


function mostrarMensaje(elemento, texto, tipo) {
    elemento.textContent = texto;
    elemento.className = "mensaje" + (tipo ? " " + tipo : "");
}

async function pedir(url, opciones) {
    let respuesta;
    try {
        respuesta = await fetch(url, opciones);
    } catch (e) {
        throw new Error("No se pudo conectar con el servidor.");
    }

    const texto = await respuesta.text();
    let datos = null;
    try {
        datos = texto ? JSON.parse(texto) : null;
    } catch (e) {
        datos = texto;
    }

    if (!respuesta.ok) {
        let mensaje = datos && datos.message ? datos.message : "";
        if (!mensaje) {
            if (respuesta.status === 400) mensaje = "Datos inválidos.";
            else if (respuesta.status === 401) mensaje = "Email o contraseña incorrectos.";
            else if (respuesta.status === 404) mensaje = "No se encontró el recurso.";
            else mensaje = "Error del servidor (" + respuesta.status + ").";
        }
        throw new Error(mensaje);
    }
    return datos;
}

function enviarJSON(metodo, cuerpo) {
    return {
        method: metodo,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(cuerpo)
    };
}


function obtenerUsuario() {
    try {
        const guardado = localStorage.getItem(CLAVE_SESION);
        return guardado ? JSON.parse(guardado) : null;
    } catch (e) {
        return null;
    }
}

function mostrarVistaSegunSesion() {
    const usuario = obtenerUsuario();
    if (usuario) {
        vistaAuth.hidden = true;
        vistaApp.hidden = false;
        nombreUsuario.textContent = usuario.nombre;
        cargarLibros();
    } else {
        vistaApp.hidden = true;
        vistaAuth.hidden = false;
    }
}


registroForm.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    mostrarMensaje(msgRegistro, "", "");

    const nuevo = {
        nombre: document.getElementById("regNombre").value.trim(),
        email: document.getElementById("regEmail").value.trim(),
        password: document.getElementById("regPassword").value
    };

    try {
        await pedir(API_USUARIOS, enviarJSON("POST", nuevo));
        mostrarMensaje(msgRegistro, "Cuenta creada. Ya podés iniciar sesión.", "exito");
        registroForm.reset();
    } catch (e) {
        mostrarMensaje(msgRegistro, e.message, "error");
    }
});

loginForm.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    mostrarMensaje(msgLogin, "", "");

    const credenciales = {
        email: document.getElementById("logEmail").value.trim(),
        password: document.getElementById("logPassword").value
    };

    try {
        const usuario = await pedir(API_USUARIOS + "/login", enviarJSON("POST", credenciales));
        localStorage.setItem(CLAVE_SESION, JSON.stringify(usuario));
        loginForm.reset();
        mostrarVistaSegunSesion();
    } catch (e) {
        mostrarMensaje(msgLogin, e.message, "error");
    }
});

btnCerrarSesion.addEventListener("click", () => {
    localStorage.removeItem(CLAVE_SESION);
    librosMostrados = [];
    tablaLibros.replaceChildren();
    cancelarEdicion();
    mostrarVistaSegunSesion();
});


async function cargarLibros() {
    const usuario = obtenerUsuario();
    if (!usuario) return;

    mostrarMensaje(msgLista, "", "");
    try {
        const libros = await pedir(API_LIBROS + "/usuario/" + usuario.id);
        dibujarTabla(libros);
    } catch (e) {
        mostrarMensaje(msgLista, e.message, "error");
    }
}

buscarForm.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const usuario = obtenerUsuario();
    if (!usuario) return;

    const texto = buscarTexto.value.trim();
    mostrarMensaje(msgLista, "", "");

    try {
        const url = API_LIBROS + "/usuario/" + usuario.id + "/buscar?texto=" + encodeURIComponent(texto);
        const libros = await pedir(url);
        dibujarTabla(libros);
        mostrarMensaje(msgLista, libros.length + " resultado(s) para \"" + texto + "\".", "");
    } catch (e) {
        mostrarMensaje(msgLista, e.message, "error");
    }
});

btnLimpiarBusqueda.addEventListener("click", () => {
    buscarTexto.value = "";
    cargarLibros();
});

function crearCelda(texto) {
    const td = document.createElement("td");
    td.textContent = texto;
    return td;
}

function dibujarTabla(libros) {
    librosMostrados = libros;
    tablaLibros.replaceChildren();

    if (libros.length === 0) {
        const fila = document.createElement("tr");
        const celda = document.createElement("td");
        celda.colSpan = 6;
        celda.className = "vacio";
        celda.textContent = "No hay libros para mostrar.";
        fila.appendChild(celda);
        tablaLibros.appendChild(fila);
        return;
    }

    libros.forEach((libro) => {
        const fila = document.createElement("tr");

        // Portada
        const celdaFoto = document.createElement("td");
        if (libro.urlFoto) {
            const img = document.createElement("img");
            img.src = libro.urlFoto;
            img.alt = "Portada de " + libro.nombre;
            celdaFoto.appendChild(img);
        }
        fila.appendChild(celdaFoto);

        fila.appendChild(crearCelda(libro.nombre));
        fila.appendChild(crearCelda(libro.autor));
        fila.appendChild(crearCelda(libro.genero));
        fila.appendChild(crearCelda(libro.anioDeCreacion));

        const celdaAcciones = document.createElement("td");

        const btnEditar = document.createElement("button");
        btnEditar.type = "button";
        btnEditar.className = "btn-link btn-editar";
        btnEditar.textContent = "Editar";
        btnEditar.addEventListener("click", () => prepararEdicion(libro.id));

        const btnEliminar = document.createElement("button");
        btnEliminar.type = "button";
        btnEliminar.className = "btn-link btn-eliminar";
        btnEliminar.textContent = "Eliminar";
        btnEliminar.addEventListener("click", () => eliminarLibro(libro.id));

        celdaAcciones.appendChild(btnEditar);
        celdaAcciones.appendChild(btnEliminar);
        fila.appendChild(celdaAcciones);

        tablaLibros.appendChild(fila);
    });
}



libroForm.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    mostrarMensaje(msgLibro, "", "");

    const usuario = obtenerUsuario();
    if (!usuario) return;

    const libro = {
        nombre: inputNombre.value.trim(),
        autor: inputAutor.value.trim(),
        genero: inputGenero.value.trim(),
        anioDeCreacion: Number(inputAnio.value),
        urlFoto: inputUrlFoto.value.trim(),
        usuarioId: usuario.id
    };

    const editando = libroId.value !== "";

    try {
        if (editando) {
            await pedir(API_LIBROS + "/" + libroId.value, enviarJSON("PUT", libro));
            mostrarMensaje(msgLibro, "Libro modificado correctamente.", "exito");
        } else {
            await pedir(API_LIBROS, enviarJSON("POST", libro));
            mostrarMensaje(msgLibro, "Libro guardado correctamente.", "exito");
        }
        cancelarEdicion(false);
        buscarTexto.value = "";
        cargarLibros();
    } catch (e) {
        mostrarMensaje(msgLibro, e.message, "error");
    }
});

function prepararEdicion(id) {
    const libro = librosMostrados.find((l) => l.id === id);
    if (!libro) return;

    libroId.value = libro.id;
    inputNombre.value = libro.nombre;
    inputAutor.value = libro.autor || "";
    inputGenero.value = libro.genero;
    inputAnio.value = libro.anioDeCreacion;
    inputUrlFoto.value = libro.urlFoto || "";

    tituloFormulario.textContent = "Editar libro";
    btnGuardar.textContent = "Guardar cambios";
    btnCancelar.hidden = false;
    mostrarMensaje(msgLibro, "", "");
    libroForm.scrollIntoView({ behavior: "smooth" });
}


function cancelarEdicion(limpiarMensaje = true) {
    libroForm.reset();
    libroId.value = "";
    tituloFormulario.textContent = "Agregar un libro";
    btnGuardar.textContent = "Guardar libro";
    btnCancelar.hidden = true;
    if (limpiarMensaje) mostrarMensaje(msgLibro, "", "");
}

btnCancelar.addEventListener("click", () => cancelarEdicion());


async function eliminarLibro(id) {
    if (!confirm("¿Seguro que querés eliminar este libro?")) return;

    try {
        await pedir(API_LIBROS + "/" + id, { method: "DELETE" });
        mostrarMensaje(msgLista, "Libro eliminado correctamente.", "exito");
        // Si se estaba editando justo ese libro, se cancela la edición
        if (libroId.value === String(id)) cancelarEdicion();
        const usuario = obtenerUsuario();
        const libros = await pedir(API_LIBROS + "/usuario/" + usuario.id);
        dibujarTabla(libros);
    } catch (e) {
        mostrarMensaje(msgLista, e.message, "error");
    }
}


mostrarVistaSegunSesion();