package com.wyble.procesagro;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wyble.procesagro.helpers.DB;
import com.wyble.procesagro.helpers.Webservice;
import com.wyble.procesagro.models.Convocatoria;
import com.wyble.procesagro.models.MiPasoOferta;
import com.wyble.procesagro.models.Oferta;
import com.wyble.procesagro.models.PasoOferta;
import com.wyble.procesagro.models.Servicio;
import com.wyble.procesagro.models.Tramite;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.valueOf;


public class MainActivity extends ActionBarActivity{

    private static final String SERVICIO1_URL = "http://google.com/";

    private static final String SERVICIO2_URL = "https://www.dane.gov.co/index.php/agropecuario-alias/sistema-de-informacion-de-precios-sipsa";

    private static final String SERVICIO3_URL = "http://www.agronet.gov.co/agronetweb1/Agromapas.aspx";

    private static final String SERVICIO4_URL = "http://www.siembra.gov.co/";

    private static final String CONVOCATORIAS_URL = "http://tucompualdia.com/aplicaciones/procesAgroWebService/convocatorias.php";

    private static final String OFERTAS_URL = "http://tucompualdia.com/aplicaciones/procesAgroWebService/ofertasinstitucionales.php";

    private static final String PASOS_OFERTAS_URL = "http://tucompualdia.com/aplicaciones/procesAgroWebService/pasosofertas.php";

    private static final String SERVICIOS_URL = "http://tucompualdia.com/aplicaciones/procesAgroWebService/servicios.php";

    private static final String CONVOCATORIAS_TABLE = "convocatorias";

    private static final String OFERTAS_TABLE = "ofertas";

    private static final String PASOS_OFERTAS_TABLE = "pasos_ofertas";

    private static final String MIS_PASOS_OFERTA_TABLE = "mis_pasos_ofertas";

    private static final String SERVICIOS_TABLE = "servicios";

    private static final String TRAMITE_TABLE = "traites";

    private ArrayList<HashMap> tables;

    private DB db;

    private Oferta oferta1;

    private Oferta oferta2;

    private Oferta oferta3;

    private Oferta oferta4;

    private Tramite tramite;

    private EditText searchText;

    private Button searchBtn;

    Button callView1;
    Button callView2;
    Button callView3;
    Button callView4;
    Button callView5;
    Button callView6;
    Button callView7;
    Button callView8;
    Button callView9;
    Button callView10;
    Button AboutCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Just for testing, allow network access in the main thread
        // NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tables = new ArrayList<HashMap>();

        Webservice wsConvocatorias = new Webservice(CONVOCATORIAS_URL);
        Webservice wsOfertas = new Webservice(OFERTAS_URL);
        Webservice wsPasosOfertas = new Webservice(PASOS_OFERTAS_URL);
        Webservice wsServicios = new Webservice(SERVICIOS_URL);

        HashMap<String, JSONArray> hmConvocatorias = new HashMap();
        HashMap<String, JSONArray> hmOfertas = new HashMap();
        HashMap<String, JSONArray> hmPasosOfertas = new HashMap();
        HashMap<String, JSONArray> hmMisPasosOfertas = new HashMap();
        HashMap<String, JSONArray> hmServicios = new HashMap();
        HashMap<String, JSONArray> hmTramite = new HashMap();

        hmConvocatorias.put(CONVOCATORIAS_TABLE, wsConvocatorias.parseJsonText(wsConvocatorias.getJsonText()));
        hmOfertas.put(OFERTAS_TABLE, wsOfertas.parseJsonText(wsOfertas.getJsonText()));
        hmPasosOfertas.put(PASOS_OFERTAS_TABLE, wsPasosOfertas.parseJsonText(wsPasosOfertas.getJsonText()));
        try {
            hmMisPasosOfertas.put(MIS_PASOS_OFERTA_TABLE, new JSONArray("[{ 'paso_oferta_id' : '', 'is_checked' : '' }]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hmServicios.put(SERVICIOS_TABLE, wsServicios.parseJsonText(wsServicios.getJsonText()));
        Tramite initTramite = new Tramite();
        hmTramite.put(TRAMITE_TABLE, initTramite.toJSONArray());

        tables.add(hmConvocatorias);
        tables.add(hmOfertas);
        tables.add(hmPasosOfertas);
        tables.add(hmMisPasosOfertas);
        tables.add(hmServicios);
        tables.add(hmTramite);

        db = new DB(this, tables);
        this.initDataTable(hmConvocatorias);
        this.initDataTable(hmOfertas);
        this.initDataTable(hmPasosOfertas);
        this.initDataTable(hmServicios);

        ArrayList<PasoOferta> pasos_ofertas = this.getPasosOferta();
        //this.initMisPasosOfertas(pasos_ofertas);

        final ArrayList<Convocatoria> convocatorias = this.getConvocatorias();
        ArrayList<Oferta> ofertas = this.getOfertas();
        ArrayList<Servicio> servicios = this.getServicios();
        ArrayList<Tramite> tramites = this.getTramites();

        if (tramites.size() > 0) {
            tramite = tramites.get(tramites.size() - 1);
        } else {
            tramite = initTramite;
        }

        oferta1 = ofertas.get(0);
        oferta2 = ofertas.get(1);
        oferta3 = ofertas.get(2);
        oferta4 = ofertas.get(3);

        searchText = (EditText) findViewById(R.id.editText);
        searchBtn = (Button) findViewById(R.id.buscar_button);

        callView1= (Button) findViewById(R.id.row1_button1);//row1
        callView2= (Button) findViewById(R.id.row1_button2);//row1
        callView3= (Button) findViewById(R.id.row2_button1);//row2
        callView4= (Button) findViewById(R.id.row2_button2);//row2

        callView5= (Button) findViewById(R.id.row3_button1);//row3
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(convocatorias.size());
        callView5.setText(convocatorias.get(index).getTitulo() + "\n" + convocatorias.get(index).getDescripcionCorta());

        callView6= (Button) findViewById(R.id.row4_button1);//row4
        callView7= (Button) findViewById(R.id.row5_button1);//row5
        callView8= (Button) findViewById(R.id.row5_button2);//row5
        callView9= (Button) findViewById(R.id.row6_button1);//row6
        callView10= (Button) findViewById(R.id.row6_button2);//row6
        AboutCall= (Button) findViewById(R.id.acercade_btn);//About button

        callView1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("URL_PARAMETER", SERVICIO1_URL);
                startActivity(intent);
            }
        });

        callView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("URL_PARAMETER", SERVICIO2_URL);
                startActivity(intent);
            }
        });

        callView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("URL_PARAMETER", SERVICIO3_URL);
                startActivity(intent);
            }
        });

        callView4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("URL_PARAMETER", SERVICIO4_URL);
                startActivity(intent);
            }
        });

        callView5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToCall = new Intent(MainActivity.this, CallActivity.class);
                intentToCall.putExtra("CONVOCATORIAS", convocatorias);
                startActivity(intentToCall);
            }
        });

        callView6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToForm = new Intent(MainActivity.this, Call_Form1Activity.class);
                intentToForm.putExtra("TRAMITE", tramite);
                startActivity(intentToForm);
            }
        });

        callView7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToDeals1 = new Intent(MainActivity.this, DealsActivity.class);
                intentToDeals1.putExtra("OFERTA", oferta1);
                startActivity(intentToDeals1);
            }
        });

        callView8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToDeals2 = new Intent(MainActivity.this, DealsActivity.class);
                intentToDeals2.putExtra("OFERTA", oferta2);
                startActivity(intentToDeals2);
            }
        });

        callView9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToDeals3 = new Intent(MainActivity.this, DealsActivity.class);
                intentToDeals3.putExtra("OFERTA", oferta3);
                startActivity(intentToDeals3);
            }
        });

        callView10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToDeals4 = new Intent(MainActivity.this, DealsActivity.class);
                intentToDeals4.putExtra("OFERTA", oferta4);
                startActivity(intentToDeals4);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String textToSearch = String.valueOf(searchText.getText());
                Intent intentToMainSearch = new Intent(MainActivity.this, MainSearch.class);
                intentToMainSearch.putExtra("TEXTO_BUSCAR", textToSearch);
                startActivity(intentToMainSearch);
            }
        });

        AboutCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentToAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentToAbout);
            }
        });

    }

    private ArrayList<Convocatoria> getConvocatorias() {
        ArrayList convocatorias = new ArrayList();
        ArrayList<HashMap> data = db.getAllData(CONVOCATORIAS_TABLE);
        for (HashMap d : data) {
            convocatorias.add(new Convocatoria(
                    Integer.parseInt(d.get("autoId").toString()),
                    d.get("tituloConvocatoria").toString(),
                    d.get("descripcion").toString(),
                    d.get("descripcionLarga").toString(),
                    d.get("urlConvocatoria").toString(),
                    d.get("usuario_id").toString()
            ));
        }

        db.close();
        return convocatorias;
    }

    private ArrayList<Oferta> getOfertas() {
        ArrayList ofertas = new ArrayList();
        ArrayList<HashMap> data = db.getAllData(OFERTAS_TABLE);
        for (HashMap d : data) {
            ofertas.add(new Oferta(
                    Integer.parseInt(d.get("autoId").toString()),
                    d.get("usuario_id").toString(),
                    d.get("tituloOferta").toString(),
                    d.get("descripcionOferta").toString(),
                    d.get("urlAudioOferta").toString(),
                    d.get("urlOferta").toString(),
                    getPasosOfertaByOfertaId(d.get("id").toString())
            ));
        }

        /*{"id":"1","usuario_id":"2","tituloOferta":"COMPRA DE ARTICULOS","descripcionOferta":"sadasdas","urlAudioOferta":"http:\/\/gos.com","urlOferta":"http:\/\/gos.com"},*/

        db.close();
        return ofertas;
    }

    private ArrayList<PasoOferta> getPasosOfertaByOfertaId(String id) {
        ArrayList<PasoOferta> pasosOfertas = new ArrayList<PasoOferta>();
        ArrayList<HashMap> data = db.getDataByValue(PASOS_OFERTAS_TABLE, "ofertaInstitucional_id", id);
        for (HashMap d : data) {
            pasosOfertas.add(new PasoOferta(
                    Integer.parseInt(d.get("autoId").toString()),
                    d.get("tituloPasos").toString(),
                    d.get("descripcionPaso").toString(),
                    d.get("urlPaso").toString()
            ));
        }
        db.close();
        return pasosOfertas;
    }

    private ArrayList<PasoOferta> getPasosOferta() {
        ArrayList pasosOfertas = new ArrayList();
        ArrayList<HashMap> data = db.getAllData(PASOS_OFERTAS_TABLE);
        for (HashMap d : data) {
            pasosOfertas.add(new PasoOferta(
                    Integer.parseInt(d.get("autoId").toString()),
                    d.get("tituloPasos").toString(),
                    d.get("descripcionPaso").toString(),
                    d.get("urlPaso").toString()
            ));
        }
        db.close();
        return pasosOfertas;
    }

    private ArrayList<Servicio> getServicios() {
        ArrayList servicios = new ArrayList();
        ArrayList<HashMap> data = db.getAllData(SERVICIOS_TABLE);
        for (HashMap d : data) {
            servicios.add(new Servicio(
                    Integer.parseInt(d.get("autoId").toString()),
                    d.get("usuario_id").toString(),
                    d.get("tituloServicio").toString(),
                    d.get("descripcionServicio").toString(),
                    d.get("urlAudioServicio").toString(),
                    d.get("urlServicio").toString()
            ));
        }

        /*{"id":"2","usuario_id":"2","tituloServicio":"Servicio 1","descripcionServicio":"DEscripcion del servicio","urlAudioServicio":"https:\/\/www.google.com\/glass\/start\/","urlServicio":"http:\/\/audio.com"}*/

        db.close();
        return servicios;
    }

    private ArrayList<Tramite> getTramites() {
        ArrayList tramites = new ArrayList();
        ArrayList<HashMap> data = db.getAllData(TRAMITE_TABLE);

        for (HashMap d : data) {
            Tramite tramite = new Tramite();
            tramite.setId(Integer.parseInt(d.get("autoId").toString()));
            tramite.setIca(d.get("ica3101").toString());
            tramite.setNombreFinca(d.get("nombreFinca").toString());
            tramite.setNombrePropietario(d.get("nombrePropietarioFinca").toString());
            tramite.setCedulaPropietario(d.get("cedulaPropietarioFinca").toString());
            tramite.setFijoPropietario(d.get("telefonoFijoPropietario").toString());
            tramite.setCelularPropietario(d.get("telefonoCelularPropietario").toString());
            tramite.setMunicipio(d.get("municipioVereda").toString());
            tramite.setDepartamento(d.get("departamento").toString());
            tramite.setNombreSolicitante(d.get("nombreSolicitante").toString());
            tramite.setCedulaSolicitante(d.get("cedulaSolicitante").toString());
            tramite.setFijoSolicitante(d.get("telefonoFijoSolicitante").toString());
            tramite.setCelularSolicitante(d.get("telefonoCelularSolicitante").toString());
            tramite.setMenor1Bovinos(Integer.parseInt(d.get("menUnoBovino").toString()));
            tramite.setEntre12Bovinos(Integer.parseInt(d.get("unoDosBovino").toString()));
            tramite.setEntre23Bovinos(Integer.parseInt(d.get("dosTresBovino").toString()));
            tramite.setMayores3Bovinos(Integer.parseInt(d.get("tresMayorBovino").toString()));
            tramite.setMenor1Bufalino(Integer.parseInt(d.get("menUnoBufalino").toString()));
            tramite.setEntre12Bufalino(Integer.parseInt(d.get("unoDosBufalino").toString()));
            tramite.setEntre23Bufalino(Integer.parseInt(d.get("dosTresBufalino").toString()));
            tramite.setMayor3Bufalino(Integer.parseInt(d.get("tresMayorBufalino").toString()));
            tramite.setPrimeraVez(Integer.parseInt(d.get("jusPrimera").toString()));
            tramite.setNacimiento(Integer.parseInt(d.get("jusNacimiento").toString()));
            tramite.setCompra(Integer.parseInt(d.get("jusCompraAnimales").toString()));
            tramite.setPerdidaDIN(Integer.parseInt(d.get("jusPerdidaDin").toString()));
            tramite.setJustificacion(d.get("justificacion").toString());
            tramite.setTerminos(Boolean.parseBoolean(d.get("terminos").toString()));
            tramites.add(tramite);
        }
        db.close();
        return tramites;
    }

    private void initDataTable(HashMap hmTable) {
        String tableName = null;
        JSONArray tableData = null;

        Set<Map.Entry> ent = hmTable.entrySet();
        for (Map.Entry e : ent) {
            tableName = (String) e.getKey();
            tableData = (JSONArray) e.getValue();
        }
        if (tableData != null) {
            db.emptyData(tableName);
            db.insertData(tableName, tableData);
        }
    }

    private ArrayList<MiPasoOferta> getMisPasosOfertasByPasoOferta(String paso_oferta_id) {
        ArrayList mis_pasos_ofertas = new ArrayList();
        ArrayList<HashMap> data = db.getDataByValue(MIS_PASOS_OFERTA_TABLE, "paso_oferta_id", paso_oferta_id);
        for (HashMap d : data) {
            mis_pasos_ofertas.add(new MiPasoOferta(
                    Integer.parseInt(d.get("paso_oferta_id").toString()),
                    Boolean.parseBoolean(d.get("is_checked").toString())
            ));
        }
        db.close();
        return mis_pasos_ofertas;
    }

    /*private void initMisPasosOfertas(ArrayList<PasoOferta> pasosOfertas) {
        for (PasoOferta pasoOferta : pasosOfertas) {
            ArrayList mis_pasos_ofertas = this.getMisPasosOfertasByPasoOferta(valueOf(pasoOferta.getId()));

            MiPasoOferta miPasoOferta = new MiPasoOferta(pasoOferta.getId(), FALSE);
            db.insertData(MIS_PASOS_OFERTA_TABLE, miPasoOferta.toJSONArray());
        }
        db.close();
    }*/

}
