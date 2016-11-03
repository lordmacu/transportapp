package co.cristiangarcia.hdtransmileniopro.helpers;

/**
 * Created by cristiangarcia on 25/10/16.
 */



public class ComunicadorRuta {
    // Step 1 - This interface defines the type of messages I want to communicate to my owner
    public interface ComunicadorRutaListener {
        // These methods are the different events and need to pass relevant arguments with the event

        public void onSelectedRoute(String ruta);
    }


     private ComunicadorRutaListener listener;

     public ComunicadorRuta() {
        this.listener = null; // set null listener
    }


     public void setComunicadorRutaListener(ComunicadorRutaListener listener) {
        this.listener = listener;
    }

}