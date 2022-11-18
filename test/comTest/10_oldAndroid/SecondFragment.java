package com.student.tool.MangaServer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.student.tool.MangaServer.connect.Main_Base;
import com.student.tool.MangaServer.connect.Main_Base_interface;
import com.student.tool.MangaServer.connect.memory.Storage;

public class SecondFragment extends Fragment implements Main_Base_interface {

    private static final String ACTIVITY_TAG = "Manga SecondFragment";
    private Storage _storage = null;
    private Main_Base _engine = null;
    private MainActivity _gui_main = null;

    private int _size_height;
    private int _size_width;
    private int shift_plus_y;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(SecondFragment.ACTIVITY_TAG, "onCreate start");

        _gui_main = (MainActivity) getActivity();
        _storage = _gui_main.getStorage();
        _engine = _gui_main.getEngine();
        _engine.setModeInt(2);

        _engine.setDualEngine(this);
        initial_touch_range((int) _gui_main.getPictureWidth(), (int) _gui_main.getPictureHeight());

        _size_height = (int) _gui_main.getPictureHeight();
        _size_width = (int) _gui_main.getPictureWidth();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    private static int screen_height_mid = 0;
    private static int screen_width_mid = 0;
    private static int screen_height_three_one = 0;
    private static int screen_height_three_two = 0;

    private static void initial_touch_range(int width, int height)
    {
        screen_height_mid      = (int) (height / 2.0);
        screen_width_mid       = (int) ( width / 2.0);
        screen_height_three_one = (int) ( height / 3.0);
        screen_height_three_two = (int) ( height / 3.0 * 2);
    }

    private ImageView _image_01 = null;
    private ImageView _image_02 = null;
    private TextView _label_01 = null;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(SecondFragment.ACTIVITY_TAG, "onViewCreated start");

        _image_01 = (ImageView) view.findViewById(R.id.second_image_01);
        _image_02 = (ImageView) view.findViewById(R.id.second_image_02);

        _image_01.setImageMatrix(new Matrix());
        _image_02.setImageMatrix(new Matrix());

        _label_01 = (TextView)  view.findViewById(R.id.second_label_01);
        _label_01.setX(0);
        _label_01.setY(0);
        //_label_01.setY((float) (-_size_width*8.0/100.0));
        shift_plus_y = (int) (_size_height*4.0/100.0);

        view.findViewById(R.id.back_screen_dual).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() != MotionEvent.ACTION_DOWN) return true;

                int posi_x = (int) event.getX();
                int posi_y = (int) event.getY();

                if (posi_x <= screen_width_mid) {
                    // nextDualPage, prevDualPage
                    if (posi_y <= screen_height_mid) {
                        Log.d(SecondFragment.ACTIVITY_TAG, "send_command nextDualPage");
                        _engine.send_command("nextDualPage");
                    } else {
                        Log.d(SecondFragment.ACTIVITY_TAG, "send_command prevDualPage");
                        _engine.send_command("prevDualPage");
                    }
                } else {
                    // next page, single_page,prev page
                    if (posi_y <= screen_height_three_one) {
                        Log.d(SecondFragment.ACTIVITY_TAG, "send_command nextPage");
                        _engine.send_command("nextPage");
                    } else if (posi_y <= screen_height_three_two) {
                        if(!_engine.have_send_command()) {
                            Log.d(SecondFragment.ACTIVITY_TAG, "send_command switch_occur");
                            onBackPressed();
                        }
                    } else {
                        Log.d(SecondFragment.ACTIVITY_TAG, "send_command prevPage");
                        _engine.send_command("prevPage");
                    }
                }
                return true;
            }
        });
        _handle = new Handler_Me(_label_01, _image_01, _image_02, _storage, _size_width, _size_height, shift_plus_y);

        show_image_on_screen(_gui_main.getShowIndex(), _gui_main.getShowIsNext(), false);

        //NavHostFragment.findNavController(SecondFragment.this)
        //       .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    private boolean switch_occur = false;
    private Handler_Me  _handle = null;

    static class Runable_Me implements Runnable {

        Handler_Me _handle = null;

        Runable_Me(Handler_Me handle) {
            _handle = handle;
        }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            _handle.sendMessage(msg);
        }
    }
    static class Handler_Me extends Handler {

        TextView _label;
        ImageView _image_01;
        ImageView _image_02;
        String _text = null;
        Storage _storage;
        int _index = 0;
        boolean _is_next = false;
        int _width;
        int _height;
        int shift_plus_y;

        Handler_Me(TextView label, ImageView image_01, ImageView image_02, Storage storage, int width, int height, int plus_y)
        {
            _label = label;
            _image_01 = image_01;
            _image_02 = image_02;
            _storage = storage;
            _width = width;
            _height = height;
            shift_plus_y = plus_y;
        }

        void setIndex(int index, boolean is_next){ _index = index; _is_next = is_next; }
        void setText(String str){ _text = str; }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1)
            {
                Bitmap bmp = _storage.get_memory(_index);
                _image_01.setImageBitmap(bmp);
                boolean is_shift = true;
                if(bmp != null)
                {
                    _image_01.setX(0);
                    if((_height-bmp.getHeight())/2.0 > shift_plus_y) {
                        _image_01.setY((float) (_height/2.0));
                    } else {
                        is_shift = false;
                        _image_01.setY(_height - bmp.getHeight());
                    }
                }
                if(_is_next)
                {
                    Bitmap bmp2 = _storage.get_memory(_index+1);
                    _image_02.setImageBitmap(bmp2);
                    if(bmp2 != null && bmp != null)
                    {
                        _image_02.setX(0);
                        if(is_shift) {
                            _image_02.setY((float) (_height/2.0 - bmp2.getHeight()));
                        } else {
                            _image_02.setY(_height  - bmp.getHeight() - bmp2.getHeight());
                        }
                    }
                } else {
                    _image_02.setImageBitmap(null);
                }
                if(_text != null)
                {
                    _label.setText(_text);
                }
            }
        }
    }

    public synchronized void show_image_on_screen(int index, boolean is_next, boolean from_thread)
    {
        if (switch_occur) {
            Log.d(SecondFragment.ACTIVITY_TAG, "show_image_on_screen switch_occur");
            switch_occur = false;
            _gui_main.setArgumentForShow(index, is_next);
            return;
        }

        Log.d("FirstFragment", "show_image_on_screen");

        if (from_thread) {

            Log.d(SecondFragment.ACTIVITY_TAG, "show_image_on_screen from non gui thread");
            _handle.setIndex(index, is_next);
            _handle.setText(_engine.getLabelString());
            Thread t1 = new Thread(new Runable_Me(_handle));
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                Log.d(SecondFragment.ACTIVITY_TAG, "show_image_on_screen error\n" + e);
                _gui_main._close_program();
            }
        } else {
            Log.d(SecondFragment.ACTIVITY_TAG, "show_image_on_screen from gui");
            Bitmap bmp = _storage.get_memory(index);
            _image_01.setImageBitmap(bmp);
            boolean is_shift = true;
            if(bmp != null)
            {
                _image_01.setX(0);
                if((_size_height-bmp.getHeight())/2.0 > shift_plus_y) {
                    _image_01.setY((float) (_size_height/2.0));
                } else {
                    is_shift = false;
                    _image_01.setY(_size_height - bmp.getHeight());
                }
            }
            if(is_next)
            {
                Bitmap bmp2 = _storage.get_memory(index+1);
                _image_02.setImageBitmap(bmp2);
                if(bmp2 != null && bmp != null)
                {
                    _image_02.setX(0);
                    if(is_shift) {
                        _image_02.setY((float) (_size_height/2.0 - bmp2.getHeight()));
                    } else {
                        _image_02.setY(_size_height  - bmp.getHeight() - bmp2.getHeight());
                    }
                }
            } else {
                _image_02.setImageBitmap(null);
            }
            _label_01.setText(_engine.getLabelString());
        }
    }

    public void onBackPressed()
    {
        switch_occur = true;
        _engine.switch_page_show_mode();
        _engine.setModeInt(1);
        //getFragmentManager().popBackStack();
       // getActivity().onBackPressed();
        NavHostFragment.findNavController(SecondFragment.this)
               .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    public void _close_program(){}
    public void send_command_fail()
    {
        Log.d(SecondFragment.ACTIVITY_TAG, "send_command_fail");
    }
}