/* 
    Document   : javascript
    Created on : 29/05/2023, 10:00 AM
    Author     : Alex Mendoza
    Description:
        javascript para el módulo de Solicitud de Autorización Servicio de Protección Personal
*/

function readyFn( jQuery ) {
             
}

$( document ).ready( readyFn );
// or:
//$( window ).on( "load", readyFn ); 

function showDlgMarcoDialogMapa() {
    $("div[id*='dlgMapa'] > .ui-dialog-titlebar ").attr("style", "margin:0!important; padding: 5px!important; color:#fff!important; background:#2399e5!important;");
    $("div[id*='dlgMapa'] > .ui-dialog-titlebar > .ui-dialog-title").attr("style", "margin:0!important; font-size:110%!important; color:#fff!important;");
    $("div[id*='dlgMapa'] > .ui-dialog-content").attr("style", "padding:4px!important;");
    $(".ui-datatable-footer").attr("style", "padding:2px!important");
    $(".ui-state-disabled, .ui-widget-content .ui-state-disabled, .ui-widget-header .ui-state-disabled").attr("style", "opacity: .70; filter: Alpha(Opacity=70); background-image: none;");
} 

var lat_modal="", long_modal="";
var mapInitialized = false, updateController = false;
function mostrarDialogo(x, y) {
    if(x!=undefined && y!=undefined) { 
        if(x!=0 && y!=0){
            var dialogo = PF('mapDialogVar');
            lat_modal   = x;
            long_modal  = y;    
            dialogo.show();
        }
    }
 
}

function updateMap(){ 
    var x,y;
    if(lat_modal!="" && long_modal !=""){
        x = lat_modal;
        y = long_modal;
    }else{
        x = -12.09636338524496;
        y = -77.05893953059318;
    }
    var latlong_lugar = new L.LatLng(x, y);
    
    if (!mapInitialized) {
        map2 = L.map('mapa_ver').setView([x, y], 13); 
        var osm = 
                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png',    
        {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>' 
        });
        map2.addLayer(osm);
        L.marker(latlong_lugar).addTo(map2);
        
        mapInitialized = true;
    }
    if(updateController){
        // Eliminar todos los marcadores del mapa
        map2.eachLayer(function (layer) {
          if (layer instanceof L.Marker) {
            map2.removeLayer(layer);
          }
        });
//        var latlong_lugar = new L.LatLng(lugar.lat, lugar.lon);  
        L.marker(latlong_lugar).addTo(map2);
        map2.setView(latlong_lugar, 17); 
        
    }
    
}

var map,map2, ruta_real;
var marker;
function irMapa(){

        map = L.map('map_local').setView([-12.09636338524496, -77.05893953059318], 13);
        var osm = 
                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png',        
        {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        //            }).addTo(map);
        });
        map.addLayer(osm);
          marker = null;
        //funcion para el evento click
        map.on('click', function(e) {
            if (marker) {
                map.removeLayer(marker);
            }
            var lat = e.latlng.lat;
            var lng = e.latlng.lng;
            
            marker = L.marker([lat, lng], { icon: customIcon }).addTo(map);
//            marker.bindPopup('Coordenadas: ' + lat + ', ' + lng).openPopup();
            $('#txtLatitud').val(lat);
            $('#txtLatitud').prop("readonly","readonly");
            $('#txtLongitud').val(lng);
            $('#txtLongitud').prop("readonly","readonly");
//            console.log('Coordenadas:', lat, lng);
          });
        
        if($('#ruta_real').val()!== undefined  && $('#ruta_real').val()!== null && $('#ruta_real').val()!== ""){ 
        // Define el icono personalizado para el marcador
            ruta_real = $('#ruta_real').val();
            var customIcon = L.icon({
              iconUrl: ruta_real + 'icons/mark_3.png',
              iconSize: [50, 50],
              iconAnchor: [16, 32]
            });
        }
//    } 
}

function consumirApi(dep, dist) {
     
    if(dep !== undefined && dep!== null && dep!== ""){ 
//      let concat_v= encodeURI('San Juan de Lurigancho'+', '+'Lima'); 
        mostrarCarga();                                           
        var concat_v= encodeURI(dep +','+ dist); 
        var url3 = 'https://nominatim.openstreetmap.org/search?format=json&limit=10&countrycodes=PE&q='+concat_v;  

        fetch(url3 ,{ mode: 'cors'})
        .then((resp) => resp.json())
        .then(function(data) { 

            var res =data;
//                console.log(res);
            if(res.length>0){
                var lugar = res[0]; 
//                    let latlong_lugar = [lugar.lat, lugar.lon];//[-12.09636338524496, -77.05893953059318]
                var latlong_lugar = new L.LatLng(lugar.lat, lugar.lon);
//                    map.panTo([-12.09636338524496, -77.05893953059318]); 
                L.marker(latlong_lugar).addTo(map);
                map.setView(latlong_lugar, 15); 
                hideCarga();
            }

        })
        .catch(function(error) {
          console.log(error);
          hideCarga();
        }); 
    }
}

function mostrarCarga() {
//  var blockGif = document.getElementById('blockGifC');
    var overlay = $('#overlay');
    overlay.show();

  // Bloquear el elemento para evitar interacciones
    var content = $('#map_local_container');
    content.css('pointer-events', 'none');
}
function hideCarga() {
    var overlay = $('#overlay');
    overlay.hide();

  // Desbloquear el elemento
    var content = $('#map_local_container');
    content.css('pointer-events', 'auto');
}

//onkeypress="validaCampo(this,event)"
function validaCampo(x, event){
    if(x.dataset.validar == "correo"){
        validateAlphanumericCorreo(event);
    }else{
        validateNumeros(event);
    }
    
}

function bloquearBoton(id_form, id_boton, bloquear){
    if(id_form != "" && id_boton != ""  ){
        var id_comp =  id_form+''+'_'+id_boton;
        if(bloquear){
            PrimeFaces.widgets['widget_'+id_comp].jq.addClass('ui-state-disabled');
            PrimeFaces.widgets['widget_'+id_comp].jq.prop("disabled",true);
        }else{
            PrimeFaces.widgets['widget_'+id_comp].jq.removeClass('ui-state-disabled');
            PrimeFaces.widgets['widget_'+id_comp].jq.prop("disabled",false);
        }
    }
}

function validarCorreo(e) {
//  var emailInput = document.getElementById("emailInput");
    var email = e.value;
    var emailRegex = /^[A-Za-z0-9_-]+@[A-Za-z0-9_-]+\.[A-Za-z]{2,}$/;
    $('#error_msg_contacto').html("");
    if (!emailRegex.test(email)) {
//    console.log("Correo electrónico válido");
    if ($("#error").length === 0) {
        msgError('error_msg_contacto', 'Correo electrónico inválido, sólo se permiten letras, números, guión bajo, @ y punto.');
        bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', true);
      }
    
    } else {
//    console.log("Correo electrónico inválido");
        $("#error").remove();
        bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', false);
    }
}
//onkeypress="validateAlphanumericCorreo(event)"
function validateAlphanumericCorreo(event) { 
        if (event.key.length === 1) {
        var input = event.target.value + event.key;
        var pattern = /^[A-Za-z0-9@\-_\.]+$/; ///^[A-Za-z0-9@\-_]$/;//permitir solo caracteres alfanuméricos, el símbolo "@" y guiones
        
        if (!pattern.test(input)) {
            event.preventDefault();
        }else{
             $("#error_msg_contacto").html("");
        }
    }
}

function msgError(id_div_msg, msg){
    
    if(id_div_msg !="" && msg!=""){
        $('#'+id_div_msg).html('<p id="error" style="display: table-cell;color: red;">(*)'+msg+'</p>');
    } 
}

function validateNumeros(event, tipo) {
     var charCode = event.which ? event.which : event.keyCode;
        if (charCode < 48 || charCode > 57) {
          event.preventDefault(); return false;
        }
    
    var inputValue = event.target.value ;//$(this).val();

    if(tipo != null && tipo != ""){
 
        if(tipo == "fijo" || tipo == "fax"){
            if (inputValue.length >= 0 && inputValue.length< 6) {
//            $('#error_msg_contacto').html('<p id="error" style="display: table-cell;color: red;">(*)El número ingresado no es válido.</p>');
                msgError('error_msg_contacto', 'El número ingresado no es válido.');
                bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', true);
            }else{
                $("#error_msg_contacto").html("");
                bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', false);
            }
            if (inputValue.length >= 11) {
                event.preventDefault();
            }   
        }else if(tipo =="movil"){
            if (inputValue.length >= 0 && inputValue.length < 8) {
//                $('#error_msg_contacto').html('<p id="error" style="display: table-cell;color: red;">(*)El número ingresado no es válido.</p>');
                msgError('error_msg_contacto', 'El número ingresado no es válido.');
                bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', true);
            }else{
                $("#error_msg_contacto").html("");
                bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', false);
            }
            if (inputValue.length >= 14) {
                event.preventDefault();
            }
        }
    }    
        
}
function validateAlphanumeric(event) {
    if (event.key.length === 1) {
        var input = event.target.value + event.key;
        var pattern = /^[a-zA-Z0-9\s]+$/; // Incluimos \s para permitir espacios
        
        if (!pattern.test(input)) {
            event.preventDefault();
        }
    }
}

function validarAlfanumerico_letrasRequeridas(event){
    if (event.key.length === 1) {
        var input = event.target.value + event.key;
        if (!/^(?=.*[a-zA-Z]{3})[a-zA-Z0-9\s]+$/.test(input)) {
//            console.log('error');
            msgError('error_msg_accionistas', 'El campo cargo requiere digitar mínimo 3 letras.');
            bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarSocio', true);
        } else {
//            console.log('bien');
            $("#error_msg_accionistas").html("");
            bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarSocio', false);
        }
    }
}

function validaSelect(event) {
    
//    console.log(event);
    bloquearBoton('FormRegSolicAuto', 'btnLPE_AgregarTipoContacto', true);
    if(event != undefined){
        if(event != null && event != "" ){
//        if(event.value != null && event.value != "" ){
//            switch (event.value){
            switch (event){
                    case "TP_MEDCO_COR"://"8": 
                            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("data-validar", "correo");
                            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("onkeypress", "validateAlphanumericCorreo(event)"); 
                            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("onblur", "validarCorreo(this)"); 
                            break;
                    case "TP_MEDCO_FIJ"://9":
                            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onkeypress");
                            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onblur");
                            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("onkeypress","validateNumeros(event, 'fijo')"); 
                            break;
                    case "TP_MEDCO_MOV"://10":  
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onkeypress");
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onblur");
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("onkeypress","validateNumeros(event, 'movil')");
                        break;
                    case "TP_MEDCO_FAX"://"11":  
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onkeypress");
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onblur");
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("onkeypress","validateNumeros(event, 'fax')"); 
                        break;
                    default:  
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onkeypress");
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onblur");
                        PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.attr("onkeypress","validateNumeros(event)"); 
                        break;

            }
        }else{
            $("#error_msg_contacto").html("");
            PrimeFaces.widgets.widget_FormRegSolicAuto_txtLPE_Alm_DescripMedioContacto.jq.removeAttr("onblur");
       } 
   } 
   
}
