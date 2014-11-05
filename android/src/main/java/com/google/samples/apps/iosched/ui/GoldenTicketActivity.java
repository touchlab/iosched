package com.google.samples.apps.iosched.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.samples.apps.iosched.util.AccountUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import co.touchlab.droidconnyc.R;

public class GoldenTicketActivity extends Activity {

    private ImageView ticketImage;
    private TextView plusName;
    private TextView accountEmail;

    public static void callMe(Activity a)
    {
        Intent i = new Intent(a, GoldenTicketActivity.class);
        a.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golden_ticket);
        findViewById(R.id.mainViewGroup).setDrawingCacheEnabled(true);

        plusName = (TextView) findViewById(R.id.plusName);
        accountEmail = (TextView) findViewById(R.id.accountEmail);
        ticketImage = (ImageView) findViewById(R.id.ticketImage);

        showTicket();
    }


    Bitmap toBitmap(BitMatrix matrix)
    {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0;x < width - 1; x++)
        {
            for (int y = 0;y < width - 1; y++)
            {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private void showTicket()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append("BEGIN:VCARD\n").append("VERSION:3.0\n");

            try
            {
                String plusName = AccountUtils.getPlusName(this);
                this.plusName.setText(plusName);
                sb.append("FN:"+plusName +"\n");
            }
            catch (Exception e)
            {

            }
            QRCodeWriter qrwriter = new QRCodeWriter();
            String email = AccountUtils.getActiveAccountName(this);
            accountEmail.setText(email);
            sb.append("EMAIL;TYPE=PREF,INTERNET:"+ email +"\n");
            sb.append("END:VCARD");

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - 50;
            int height = size.y - 50;

            int imageSize = Math.min(width, height);

            BitMatrix encode = qrwriter.encode(sb.toString(), BarcodeFormat.QR_CODE, imageSize, imageSize);
            ticketImage.setImageBitmap(toBitmap(encode));
//            ((ImageView) view.findViewById(R.id.barcodeView)).setImageBitmap(toBitmap(encode));
        }
        catch (WriterException e)
        {
            finish();
            Toast.makeText(this, "Some error...", Toast.LENGTH_LONG).show();
        }
    }
}
