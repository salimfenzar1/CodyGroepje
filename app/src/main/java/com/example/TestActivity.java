package com.example; // Replace with your actual package name

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.DAO.DatabaseInitializer;
import com.example.DAO.StatementDAO;
import com.example.DAO.StatementRoom;
import com.example.Model.Statement;
import com.example.codycactus.R;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private StatementDAO statementDAO;
    private LinearLayout statementContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testactivity);

        statementContainer = findViewById(R.id.statement_container);
        statementDAO = StatementRoom.getInstance(this).statementDAO();

        // Populate the database if it isn't already populated
        DatabaseInitializer.populateDatabase(this);

        // Observe statements after populating the database
        observeStatements();
    }

    private void observeStatements() {
        LiveData<List<Statement>> liveDataStatements = statementDAO.getAllStatements();
        liveDataStatements.observe(this, new Observer<List<Statement>>() {
            @Override
            public void onChanged(List<Statement> statements) {
                if (statements != null && !statements.isEmpty()) {
                    displayStatements(statements);
                }
            }
        });
    }

    private void displayStatements(List<Statement> statements) {
        statementContainer.removeAllViews(); // Clear the layout before adding new views
        for (Statement statement : statements) {
            addStatementToLayout(statement);
        }
    }

    private void addStatementToLayout(Statement statement) {
        // Create and add TextView
        TextView textView = new TextView(this);
        textView.setText(formatStatement(statement));
        statementContainer.addView(textView);

        // Create and add ImageView
        ImageView imageView = new ImageView(this);
        int imageResource = Integer.parseInt(statement.imageUrl);
        Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), imageResource, 400, 400);
        imageView.setImageBitmap(bitmap);

        // Set layout parameters to scale the image appropriately
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        statementContainer.addView(imageView);
    }

    private String formatStatement(Statement statement) {
        return statement.description;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
