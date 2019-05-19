package com.techexchange.mobileapps.lab15;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener  {

    private TextView luminescence, proximity, stepCounter, azimuth, pitch, roll, pressure, ambientTemp, magnitude;
    private Sensor lightSensor, proximitySensor, rotationSensor, stepCounterSensor, magneticSensor, pressureSensor, tempSensor;
    private SensorManager sensorManager;
    private String availableSensors;
    private Button sensorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        luminescence = findViewById(R.id.luminescence_value);
        proximity = findViewById(R.id.proximity_value);
        stepCounter = findViewById(R.id.step_value);
        azimuth = findViewById(R.id.azimuth_value);
        pitch = findViewById(R.id.pitch_value);
        roll = findViewById(R.id.roll_value);
        pressure = findViewById(R.id.pressure_value);
        magnitude = findViewById(R.id.magnitude_value);
        ambientTemp = findViewById(R.id.temp_value);

        sensorButton = findViewById(R.id.button_id);
        sensorButton.setOnClickListener(v -> onButtonPressed());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        getSensorAvailability();
        availableSensors = getSensorList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float valueX;
        float valueY;
        float valueZ;
        switch (event.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                valueZ = event.values[0];
                luminescence.setText(String.format("%.2f", valueZ));
                break;
            case Sensor.TYPE_PROXIMITY:
                valueZ = event.values[0];
                proximity.setText(String.format("%.2f", valueZ));
                break;
            case Sensor.TYPE_STEP_COUNTER:
                valueZ = event.values[0];
                stepCounter.setText(String.format("%.2f", valueZ));
                break;
            case Sensor.TYPE_PRESSURE:
                valueZ = event.values[0];
                pressure.setText(String.format("%.2f", valueZ));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                valueX = event.values[0];
                valueY = event.values[1];
                valueZ = event.values[2];
                float finalValue = (float) Math.sqrt((valueX * valueX) + (valueY * valueY) + (valueZ * valueZ));
                magnitude.setText(String.format("%.2f", finalValue));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                valueZ = event.values[0];
                ambientTemp.setText(String.format("%.2f", valueZ));
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float[] rotMatrix = new float[9];
                float[] rotVals = new float[3];

                SensorManager.getRotationMatrixFromVector(rotMatrix, event.values);
                SensorManager.remapCoordinateSystem(rotMatrix,
                        SensorManager.AXIS_X, SensorManager.AXIS_Y, rotMatrix);

                SensorManager.getOrientation(rotMatrix, rotVals);
                float azimuthVal = (float) Math.toDegrees(rotVals[0]);
                float pitchVal = (float) Math.toDegrees(rotVals[1]);
                float rollVal = (float) Math.toDegrees(rotVals[2]);

                azimuth.setText(String.format("%.2f", azimuthVal));
                pitch.setText(String.format("%.2f", pitchVal));
                roll.setText(String.format("%.2f", rollVal));
                break;
        }
    }


    private void onButtonPressed(){
        new AlertDialog.Builder(this).setMessage(availableSensors)
                .setTitle(getString(R.string.sensorList))
                .setPositiveButton(getString(R.string.OK), (arg0, arg1) -> {}).show();
    }

    private String getSensorList(){
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        String sensorInfo = "";
        for (Sensor s: sensorList)
            sensorInfo += s.getName()+"\n";
        return sensorInfo;
    }

    private void getSensorAvailability(){
        if ((lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)) == null)
            luminescence.setText("N/A");
        if ((proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)) == null)
            proximity.setText("N/A");
        if ((stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)) == null)
            stepCounter.setText("N/A");
        if ((pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)) == null)
            pressure.setText("N/A");
        if ((magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) == null)
            magnitude.setText("N/A");
        if ((tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)) == null)
            ambientTemp.setText("N/A");
        if ((rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)) == null) {
            azimuth.setText("N/A");
            pitch.setText("N/A");
            roll.setText("N/A");
        }
    }
}
