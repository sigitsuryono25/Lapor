package com.lauwba.surelabs.lapor;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    Context context;
    private String sessionName = SessionManager.class.getSimpleName().toLowerCase();
    private SharedPreferences session;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;

        //inisialisasi session untuk pertama kali
        session = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);

        //untuk membuat session dapat diakses
        editor = session.edit();
    }

    public boolean simpanSession(String noKTP, String nama, String email) {
        try {
            //mempersiapkan variable dan value yang akan ditulis kedalam session
            editor.putString(Config.NO_KTP, noKTP);
            editor.putString(Config.NAMA, nama);
            editor.putString(Config.EMAIL, email);

            //menuliskan varible dan value yang telah didefinisikan kedalam session
            editor.commit();

            // jika penulisan berhasil, akan menghasilkan nilai true
            return true;
        } catch (Exception e) {
            //mencetak error kedalam logca
            e.printStackTrace();

            //kembalikan nilai false ketika terjadi kesalahan
            return false;
        }
    }

    public String getValue(String key) {
        //ini proses untuk mengambil value dari variable session
        //yang sudah berhasil dituliskan sebelumnya

        return session.getString(key, "tidak ada value");
    }

    public boolean hapusSession() {
        try {
            //menghapus semua variable dan value yang ada disession
            editor.clear();

            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean chekSession() {
        // proses pengecekan session.
        // Bila di dalam session terdapat Variabel noKTP, nama dan email
        // maka return true, bila tidak return false;

        if (session.contains(Config.NO_KTP) && session.contains(Config.NAMA)
                && session.contains(Config.EMAIL)) {
            return true;
        } else {
            return false;
        }
    }
}
