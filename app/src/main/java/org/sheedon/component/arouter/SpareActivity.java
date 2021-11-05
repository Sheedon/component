package org.sheedon.component.arouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import org.sheedon.component.R;

@Route(path = "/Test/SpareActivity")
public class SpareActivity extends AppCompatActivity {

    @Autowired(name = "id", desc = "设备ID")
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare);
    }
}