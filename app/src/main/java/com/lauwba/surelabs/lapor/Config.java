package com.lauwba.surelabs.lapor;

public class Config {

    //URL Konfigurasi
//    public static final String HOST = "http://admin119.server411.tech/index.php/";
    public static final String HOST = "http://192.168.137.1/Codeigniter/Admin119/index.php/";
    public static final String URL_REGISTRASI = HOST + "Webservice/registrasi";
    public static final String URL_LOGIN = HOST + "Webservice/login";
    public static final String URL_LAPOR = HOST + "Webservice/lapor";

    //Parameter pengiriman Registrasi
    public static final String EMAIL = "email";
    public static final String NAMA = "nama";
    public static final String NO_KTP = "noKTP";
    public static final String NO_TELP = "noTelp";
    public static final String PASSWORD = "password";

    //Parameter pengiriman Laporan
    public static final String POSISI = "posisi";
    public static final String JUMLAH_KORBAN = "jumlah_korban";
    public static final String STATUS_KORBAN = "status_korban";
    public static final String GAMBAR = "gambar";
    public static final String FILE_CONTENT = "file-content";
    public static final String KONDISI_KORBAN = "kondisi_korban";

    //parameter URL
    public static final String URL = "url";

}
