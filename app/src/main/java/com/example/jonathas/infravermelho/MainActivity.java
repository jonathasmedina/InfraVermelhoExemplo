package com.example.jonathas.infravermelho;

import android.app.Activity;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.hardware.ConsumerIrManager;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
//import com.example.android.apis.R;
/**
 * App that transmit an IR code
 *
 * <p>This demonstrates the {@link android.hardware.ConsumerIrManager android.hardware.ConsumerIrManager} class.
 *
 * <h4>Demo</h4>
 * Hardware / Consumer IR
 *
 * <h4>Source files</h4>
 * <table class="LinkTable">
 *         <tr>
 *             <td>src/com.example.android.apis/hardware/ConsumerIr.java</td>
 *             <td>Consumer IR demo</td>
 *         </tr>
 *         <tr>
 *             <td>res/any/layout/consumer_ir.xml</td>
 *             <td>Defines contents of the screen</td>
 *         </tr>
 * </table>
 */
public class MainActivity extends Activity {
    private static final String TAG = "ConsumerIrTest";
    TextView mFreqsText;
    ConsumerIrManager mCIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Pega a referência para o ConsumerIrManager
        mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);

        setContentView(R.layout.activity_main);

        //Ouvinte dos botões
        findViewById(R.id.send_button).setOnClickListener(ouvinteEnviarFreq);
        findViewById(R.id.get_freqs_button).setOnClickListener(ouvinteGetFreq);

        mFreqsText = (TextView) findViewById(R.id.freqs_text);
    }

    View.OnClickListener ouvinteEnviarFreq = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mCIR.hasIrEmitter()) {
                Log.e(TAG, "Emissor InfraVermelho não detectado.\n");
                return;
            }

            //Um padrão de series alternadas medidas em microsegundos
            int[] pattern = {0, 109, 30, 3, 341, 171, 24, 21, 625, 442, 625, 442, 625,
                    468, 625, 442, 625, 494, 572, 1614, 625, 1588, 625, 1614, 625, 494, 572, 442, 651,
                    442, 625, 442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442, 625, 494, 598,
                    442, 625, 442, 625, 520, 572, 442, 625, 442, 625, 442, 651, 1588, 625, 1614, 625,
                    1588, 625, 1614, 625, 1588, 625, 48958};

            //transmite o padrão a 5KHz
            mCIR.transmit(50000, pattern);
        }
    };
    View.OnClickListener ouvinteGetFreq = new View.OnClickListener() {
        public void onClick(View v) {
            StringBuilder b = new StringBuilder();
            if (!mCIR.hasIrEmitter()) {
                mFreqsText.setText("Emissor InfraVermelho não detectado.");
                Log.e(TAG, "Emissor InfraVermelho não detectado.\n");
                return;
            }
            //Recebe o alcance (range) de frequências disponíveis do transmissor
            ConsumerIrManager.CarrierFrequencyRange[] freqs = mCIR.getCarrierFrequencies();
            b.append("Frequências do transmissor InfraVermelho:\n");
            for (ConsumerIrManager.CarrierFrequencyRange range : freqs) {
                b.append(String.format("    %d - %d\n", range.getMinFrequency(),
                        range.getMaxFrequency()));
            }
            mFreqsText.setText(b.toString());
        }
    };
}
