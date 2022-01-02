


function escribirPiePagina(texto)
{
	
	document.getElementById("pie").innerHTML ="<p align='center'>Todos Los Derechos Reservados &copy; 2021</p>"; //=texto;
	
}


function botonSiguienteStartApp()
{

	 PublicidadJs.mostrarIntestialStartApp();
}

function mostrarActividad()
{

	 PublicidadJs.mostrarActividadAndroid();
}


function botonSiguienteAdMob()
{
      PublicidadJs.mostrarIntestialAdMob();
}

function botonSiguienteTappx()
{
       PublicidadJs.mostrarIntestialTappx();
	
}

function botonMenuTappx()
{
 PublicidadJs.mostrarIntestialTappx();

	
}

function botonMenuStartApp()
{
PublicidadJs.mostrarIntestialStartApp();

	
}


function botonMenuAdMob()
{
PublicidadJs.mostrarIntestialAdMob();

	
}
function botonAtrasStartApp()
{
PublicidadJs.mostrarIntestialStartApp();
	
}

function botonAtrasAdMob()
{
PublicidadJs.mostrarIntestialAdMob();
	
}
function botonAtrasTappx()
{

	 PublicidadJs.mostrarIntestialTappx();
}


function botonAyudaStartApp()
{
PublicidadJs.mostrarIntestialStartApp();
	
}

function botonAyudaAdmob()
{

PublicidadJs.mostrarIntestialAdMob();
}

function botonAyudaTappx()
{

	 PublicidadJs.mostrarIntestialTappx();
}

function botonAceptarAdmob()
{
 PublicidadJs.mostrarIntestialAdMob();
	
}
function botonAceptarTappx()
{
    PublicidadJs.mostrarIntestialTappx();
	
}

function botonAceptarStartApp()
{
   PublicidadJs.mostrarIntestialStartApp();
	
}

function clickBoton()
{
	//Para botones aceptar
	var botonAceptar1 = document.getElementById("botonaceptar1");
	botonAceptar1.addEventListener("click",botonAceptarTappx,false);
	
	var botonAceptar2 = document.getElementById("botonaceptar2");
	botonAceptar2.addEventListener("click",botonAceptarStartApp,false);
	
	var botonAceptar3 = document.getElementById("botonaceptar3");
	botonAceptar3.addEventListener("click",botonAceptarAdmob,false);
	
	
	var botonAceptar4 = document.getElementById("botonaceptar4");
	botonAceptar4.addEventListener("click",botonAceptarAdmob,false);
	
	
	var botonAceptar5 = document.getElementById("botonaceptar5");
	botonAceptar5.addEventListener("click",botonAceptarTappx,false);
	
	
	
	//Para botones siguientes
	
	
	var botonSiguienteP = document.getElementById("botonsiguientep");
	botonSiguienteP.addEventListener("click",botonSiguienteStartApp,false);
	
	
	var siguiente = document.getElementById("siguiente4");
	siguiente.addEventListener("click",botonSiguienteStartApp,false);
	
	
	//Para botones menu
	
	var menu4 = document.getElementById("menu4");
	menu4.addEventListener("click",botonMenuAdMob,false);
	
	
	//Para botones atras
	
	var botonAtras3 = document.getElementById("botonatras3");
	botonAtras3.addEventListener("click",botonAtrasTappx,false);
	

	
}




function cargarJs()
{
	escribirPiePagina(" ");
	
	clickBoton();
	
	
}




window.addEventListener("load",cargarJs,false);