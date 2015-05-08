package com.ramses.practica_14_hilos_factorial__fibonacci;

/*
Autor: RAMSÉS MARTÍNEZ ORTIZ (C) Mayo 2015
VERSIÓN: 1.0

Descripción: Programa de la práctica número 14 "Hilos".
    Solución al problema: "Agregar un hilo que muestra los N números de la serie de
    Fibonacci, El valor de N es el mismo del utilizado para obtener el factorial de la
    practica de hilos hecha en en clase"

Observaciones: Se utiliza un ProgressDialog para realizar el cálculo.

Compilación: se compila en tiempo de ejecucion.

Ejecución: se ejecuta desde el IDE de Android Studio con las teclas shift + F10.  (En Windows)
*/


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.*;


public class MainActivity extends Activity {
    private EditText entrada;
    private TextView salida;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada = (EditText) findViewById(R.id.entrada);
        salida = (TextView) findViewById(R.id.salida);
    }

      //Método para calcular las operacionesa
    public void calculaAmbos(View view) {
        salida.setText("");
        int n = Integer.parseInt(entrada.getText().toString());

        //Manda a llamar a la clase Factorial
        Factorial calculaFactorial = new Factorial();
        calculaFactorial.execute(n);

        //Manda a llamar a la clase Fibonacci
        //Utilizando Thread
        Fibonacci fibonacci = new Fibonacci(n);
        fibonacci.start();
    }

    //Método para calcular la sucesión de fibonacci utilizando la clase AsyncTask
    public void calcularFactorial(View view){
        salida.setText("");
        int n = Integer.parseInt(entrada.getText().toString());

        //Manda a llamar la clase Factorial
        Factorial calculaFactorial = new Factorial();
        calculaFactorial.execute(n);
    }

    //Método para calcular el número factorial utilizando la clase Thread
    public void calcularFibonacci(View view){
        salida.setText("");
        int n = Integer.parseInt(entrada.getText().toString());

        //Manda a llamar a la clase Fibonacci
        //Utilizando Thread
        Fibonacci fibonacci = new Fibonacci(n);
        fibonacci.start();
    }

    //  Empleamos  AsyncTask whit progressdialog cancel para calcular el factorial
    class Factorial extends AsyncTask<Integer, Integer, Integer> {
        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.
                    STYLE_HORIZONTAL);
            progreso.setMessage("Calculando el número factorial de "+entrada.getText().toString());
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    Factorial.this.cancel(true);
                }
            });
            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }

        @Override
        protected Integer doInBackground(Integer... n) {
            int factorial = 1;
            for (int i = 1; i <= n[0] && !isCancelled(); i++) {
                factorial *= i;
                SystemClock.sleep(200);
                publishProgress(i * 100 / n[0]);
            }
            return factorial;
        }

        @Override
        protected void onProgressUpdate(Integer... porc) {
            progreso.setProgress(porc[0]);
        }

        @Override
        protected void onPostExecute(Integer factorial) {
            progreso.dismiss();
            salida.append("\nFactorial de "+entrada.getText().toString()+" es:  "+String.valueOf(factorial) + "\n");
        }

        @Override
        protected void onCancelled() {
            salida.append("\nOperación cancelada+\n");
        }
    }

    class Fibonacci extends Thread {
        private int n;
        public Fibonacci(int n) {
            this.n=n;
        }
        //Método que permite obtener la sucesion de fibonacci hasta la posicion n
        public int[] CalculaFibonacci (int n){
            int fibonacci[] = new int[n];
            if(n==1 || n==0){
                fibonacci[0] =1;
            }else if(n>2) {
                fibonacci[0]=1;
                fibonacci[1]=1;
                for (int i=2; i<n; i++) {
                    fibonacci[i]=fibonacci[i-1]+fibonacci[i-2];
                }
            }
            return fibonacci;
        }
        //Método que imprime la sucesión de fibonacci en la pantalla
        public void imprimeFibonacci(int[] fibonacci){
            for(int i=0; i<fibonacci.length; i++){
                salida.append(String.valueOf(+fibonacci[i]+", "));
                //SystemClock.sleep(500);
            }
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    salida.append("\nSucesión de fibonacci hasta "+n+":\n");
                    imprimeFibonacci(CalculaFibonacci(n));
                    salida.append("\n");
                }
            });
        }
    }
}