package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite.exceptions.*;

public class MainActivity extends AppCompatActivity {

    private EditText etCode, etDescription, etPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCode =  findViewById(R.id.et_code);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
    }

    public void register(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "products", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        String code = etCode.getText().toString();
        String description = etDescription.getText().toString();
        String price = etPrice.getText().toString();

        try {
            if (code.isEmpty())
                throw new NullCodeEsception();
            else if (description.isEmpty())
                throw new NullDescriptionException();
            else if (price.isEmpty())
                throw new NullPriceException();

            ContentValues registry = new ContentValues();
            registry.put("code", code);
            registry.put("description", description);
            registry.put("price", price);

            database.insert("articles", null, registry);
            database.close();

            etCode.setText("");
            etDescription.setText("");
            etPrice.setText("");

            Toast.makeText(this, "Product saved successfully", Toast.LENGTH_SHORT).show();
        } catch (NullPriceException e) {
            Toast.makeText(this, "Type the product code", Toast.LENGTH_SHORT).show();
        } catch (NullCodeEsception e) {
            Toast.makeText(this, "Type the product description", Toast.LENGTH_SHORT).show();
        } catch (NullDescriptionException e) {
            Toast.makeText(this, "Type the product price", Toast.LENGTH_SHORT);
        }
    }

    public void find(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "products", null, 1);


        try(SQLiteDatabase database = admin.getWritableDatabase()) {
            String code = etCode.getText().toString();
            if(code.isEmpty())
                throw new NullCodeEsception();

            Cursor line = database.rawQuery("select description, price from articles where code = " + code, null);

            if (line.moveToFirst()){
                etDescription.setText(line.getString(0));
                etPrice.setText(line.getString(1));
            }else {
                throw new NotFoundProduct();
            }

        } catch (NullCodeEsception nullCodeEsception) {
            Toast.makeText(this, "Type the product code", Toast.LENGTH_SHORT).show();
        } catch (NotFoundProduct notFoundProduct) {
            Toast.makeText(this, "Couldn't find the product", Toast.LENGTH_SHORT).show();
        }
    }

    public void erase(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "products", null, 1);

        try(SQLiteDatabase database = admin.getWritableDatabase()) {
            String code = etCode.getText().toString();

            if(code.isEmpty())
                throw new NullCodeEsception();

            int erasedArticles = database.delete("articles", "code=" + code, null);

            etCode.setText("");
            etDescription.setText("");
            etPrice.setText("");

            if(erasedArticles == 1)
                Toast.makeText(this, "Product erased successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "This product does not exist", Toast.LENGTH_LONG).show();

        } catch (NullCodeEsception nullCodeEsception) {
            Toast.makeText(this, "Type the product code", Toast.LENGTH_SHORT).show();
        }
    }

    public void modify(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "products", null, 1);

        try(SQLiteDatabase database = admin.getWritableDatabase()) {

            String code = etCode.getText().toString();
            String description = etDescription.getText().toString();
            String price = etPrice.getText().toString();

            if (code.isEmpty())
                throw new NullCodeEsception();
            else if(description.isEmpty())
                throw new NullDescriptionException();
            else if(price.isEmpty())
                throw new NullPriceException();

            ContentValues registry = new ContentValues();

            registry.put("code", code);
            registry.put("description", description);
            registry.put("price", price);

            int savedProducts = database.update("articles", registry, "code=" + code, null);

            if(savedProducts == 1)
                Toast.makeText(this, "Product modified successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "This product does not exist", Toast.LENGTH_LONG).show();

        } catch (NullCodeEsception nullCodeEsception) {
            Toast.makeText(this, "Type the product code", Toast.LENGTH_SHORT).show();
        } catch (NullPriceException e) {
            Toast.makeText(this, "Type the product description", Toast.LENGTH_SHORT).show();
        } catch (NullDescriptionException e) {
            Toast.makeText(this, "Type the product price", Toast.LENGTH_SHORT).show();
        }


    }
}