
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.Scanner;

class Pasien {
    private String id;
    private String nama;
    private int umur;
    private String alamat;

    public Pasien() {
        id = "";
        nama = "";
        umur = 0;
        alamat = "";
    }

    public Pasien(String id, String nama, int umur, String alamat) {
        if (umur < 0 || umur > 120) {
            throw new IllegalArgumentException("Umur harus 0 sampai 120");
        }

        this.id = id;
        this.nama = nama;
        this.umur = umur;
        this.alamat = alamat;
    }

    public boolean umurValid() {
        return umur >= 0 && umur <= 120;
    }

    public void ubahAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void ubahNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getUmur() {
        return umur;
    }

    public void tampil() {
        System.out.println(id + " | " + nama + " | " + umur + " | " + alamat);
    }
}

class Dokter {
    private String id;
    private String nama;
    private String spesialis;
    private Map<LocalDate, Integer> selesai;
    private Map<LocalDate, Integer> antre;

    public Dokter() {
        id = "";
        nama = "";
        spesialis = "";
        selesai = new HashMap<>();
        antre = new HashMap<>();
    }

    public Dokter(String id, String nama, String spesialis) {
        this.id = id;
        this.nama = nama;
        this.spesialis = spesialis;
        selesai = new HashMap<>();
        antre = new HashMap<>();
    }

    public boolean tersedia(LocalDate tanggal) {
        int s = selesai.getOrDefault(tanggal, 0);
        int a = antre.getOrDefault(tanggal, 0);

        return s + a < 5;
    }

    public boolean pesanKunjungan(LocalDate tanggal) {
        if (!tersedia(tanggal)) {
            return false;
        }

        int jumlah = antre.getOrDefault(tanggal, 0);
        antre.put(tanggal, jumlah + 1);
        return true;
    }

    public void kunjunganSelesai(LocalDate tanggal) {
        int jumlahAntre = antre.getOrDefault(tanggal, 0);

        if (jumlahAntre > 0) {
            antre.put(tanggal, jumlahAntre - 1);
        }

        int jumlahSelesai = selesai.getOrDefault(tanggal, 0);
        selesai.put(tanggal, jumlahSelesai + 1);
    }

    public void batalkanPesanan(LocalDate tanggal) {
        int jumlah = antre.getOrDefault(tanggal, 0);

        if (jumlah > 0) {
            antre.put(tanggal, jumlah - 1);
        }
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void tampil() {
        LocalDate hari = LocalDate.now();

        System.out.println(id + " | " + nama + " | " + spesialis
                + " | Selesai: " + selesai.getOrDefault(hari, 0)
                + " | Antrean: " + antre.getOrDefault(hari, 0));
    }
}

class Kunjungan {
    private String id;
    private Pasien pasien;
    private Dokter dokter;
    private LocalDate tanggal;
    private String status;

    public Kunjungan() {
        id = "";
        pasien = new Pasien();
        dokter = new Dokter();
        tanggal = LocalDate.now();
        status = "Menunggu";
    }

    public Kunjungan(String id, Pasien pasien, Dokter dokter) {
        this.id = id;
        this.pasien = pasien;
        this.dokter = dokter;
        tanggal = LocalDate.now();
        status = "Menunggu";
    }

    public void mulai() {
        status = "Diproses";
    }

    public void selesai() {
        status = "Selesai";
    }

    public void batal() {
        status = "Dibatalkan";
    }

    public Pasien getPasien() {
        return pasien;
    }

    public Dokter getDokter() {
        return dokter;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void tampil() {
        System.out.println(id + " | " + pasien.getNama()
                + " | " + dokter.getNama()
                + " | " + tanggal + " | " + status);
    }
}

class RekamMedis {
    private String id;
    private Kunjungan kunjungan;
    private String diagnosis;
    private String tindakan;
    private LocalDate tanggal;

    public RekamMedis() {
        id = "";
        kunjungan = new Kunjungan();
        diagnosis = "";
        tindakan = "";
        tanggal = LocalDate.now();
    }

    public RekamMedis(String id, Kunjungan kunjungan,
                      String diagnosis, String tindakan) {
        this.id = id;
        this.kunjungan = kunjungan;
        this.diagnosis = diagnosis;
        this.tindakan = tindakan;
        tanggal = LocalDate.now();
    }

    public void ubahDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void ubahTindakan(String tindakan) {
        this.tindakan = tindakan;
    }

    public boolean lengkap() {
        return !diagnosis.isEmpty() && !tindakan.isEmpty();
    }

    public void tampil() {
        System.out.println(id + " | " + kunjungan.getPasien().getNama()
                + " | " + diagnosis + " | " + tindakan + " | " + tanggal);
    }
}

class Antrean {
    private Kunjungan[] data;
    private int depan;
    private int belakang;
    private int jumlah;
    private int kapasitas;

    public Antrean() {
        kapasitas = 100;
        data = new Kunjungan[kapasitas];
        depan = 0;
        belakang = 0;
        jumlah = 0;
    }

    public Antrean(int kapasitas) {
        if (kapasitas <= 0) {
            kapasitas = 100;
        }

        this.kapasitas = kapasitas;
        data = new Kunjungan[kapasitas];
        depan = 0;
        belakang = 0;
        jumlah = 0;
    }

    public boolean enqueue(Kunjungan k) {
        if (isFull()) {
            return false;
        }

        data[belakang] = k;
        belakang = (belakang + 1) % kapasitas;
        jumlah++;
        return true;
    }

    public Kunjungan dequeue() {
        if (isEmpty()) {
            return null;
        }

        Kunjungan k = data[depan];
        data[depan] = null;
        depan = (depan + 1) % kapasitas;
        jumlah--;

        return k;
    }

    public Kunjungan front() {
        if (isEmpty()) {
            return null;
        }

        return data[depan];
    }

    public boolean isEmpty() {
        return jumlah == 0;
    }

    public boolean isFull() {
        return jumlah == kapasitas;
    }

    public int size() {
        return jumlah;
    }

    public void tampil() {
        if (isEmpty()) {
            System.out.println("Antrean kosong");
            return;
        }

        for (int i = 0; i < jumlah; i++) {
            int posisi = (depan + i) % kapasitas;
            data[posisi].tampil();
        }
    }
}

class Klinik {
    private ArrayList<Pasien> pasien;
    private ArrayList<Dokter> dokter;
    private ArrayList<Kunjungan> kunjungan;
    private ArrayList<RekamMedis> rekam;
    private Antrean antrean;
    private int nomorKunjungan;
    private int nomorRekam;

    public Klinik() {
        pasien = new ArrayList<>();
        dokter = new ArrayList<>();
        kunjungan = new ArrayList<>();
        rekam = new ArrayList<>();
        antrean = new Antrean(100);
        nomorKunjungan = 1;
        nomorRekam = 1;
    }

    public boolean tambahPasien(Pasien p) {
        if (p == null || cariPasien(p.getId()) != null) {
            return false;
        }

        pasien.add(p);
        return true;
    }

    public boolean tambahDokter(Dokter d) {
        if (d == null || cariDokter(d.getId()) != null) {
            return false;
        }

        dokter.add(d);
        return true;
    }

    public Pasien cariPasien(String kata) {
        for (Pasien p : pasien) {
            if (p.getId().equalsIgnoreCase(kata)
                    || p.getNama().equalsIgnoreCase(kata)) {
                return p;
            }
        }

        return null;
    }

    public Dokter cariDokter(String id) {
        for (Dokter d : dokter) {
            if (d.getId().equalsIgnoreCase(id)) {
                return d;
            }
        }

        return null;
    }

    public boolean buatAntrean(String idPasien, String idDokter) {
        Pasien p = cariPasien(idPasien);
        Dokter d = cariDokter(idDokter);

        if (p == null || d == null) {
            System.out.println("Data pasien atau dokter tidak ditemukan");
            return false;
        }

        LocalDate tanggal = LocalDate.now();

        if (!d.pesanKunjungan(tanggal)) {
            System.out.println("Dokter sudah mencapai 5 kunjungan");
            return false;
        }

        Kunjungan k = new Kunjungan(
                "K" + nomorKunjungan, p, d
        );

        if (!antrean.enqueue(k)) {
            d.batalkanPesanan(tanggal);
            System.out.println("Antrean penuh");
            return false;
        }

        nomorKunjungan++;
        kunjungan.add(k);
        System.out.println("Pasien masuk antrean");
        return true;
    }

    public void prosesAntrean(String diagnosis, String tindakan) {
        Kunjungan k = antrean.dequeue();

        if (k == null) {
            System.out.println("Antrean kosong");
            return;
        }

        k.mulai();
        k.getDokter().kunjunganSelesai(k.getTanggal());
        k.selesai();

        RekamMedis r = new RekamMedis(
                "R" + nomorRekam, k, diagnosis, tindakan
        );

        nomorRekam++;
        rekam.add(r);

        System.out.println("Kunjungan selesai");
    }

    public void tampilPasien() {
        if (pasien.isEmpty()) {
            System.out.println("Belum ada pasien");
        }

        for (Pasien p : pasien) {
            p.tampil();
        }
    }

    public void tampilDokter() {
        if (dokter.isEmpty()) {
            System.out.println("Belum ada dokter");
        }

        for (Dokter d : dokter) {
            d.tampil();
        }
    }

    public void tampilAntrean() {
        antrean.tampil();
    }

    public void tampilKunjungan() {
        if (kunjungan.isEmpty()) {
            System.out.println("Belum ada kunjungan");
        }

        for (Kunjungan k : kunjungan) {
            k.tampil();
        }
    }

    public void tampilRekamMedis() {
        if (rekam.isEmpty()) {
            System.out.println("Belum ada rekam medis");
        }

        for (RekamMedis r : rekam) {
            r.tampil();
        }
    }

    public void ringkasan() {
        System.out.println("Jumlah pasien   : " + pasien.size());
        System.out.println("Jumlah dokter   : " + dokter.size());
        System.out.println("Jumlah antrean  : " + antrean.size());
        System.out.println("Jumlah kunjungan: " + kunjungan.size());
        System.out.println("Jumlah rekam    : " + rekam.size());
    }

    public void lihatDepanAntrean() {
        Kunjungan k = antrean.front();

        if (k == null) {
            System.out.println("Antrean kosong");
        } else {
            k.tampil();
        }
    }
}

public class Klinikgeran {
    static Scanner input = new Scanner(System.in);

    public static String bacaTeks(String pesan) {
        String hasil;

        do {
            System.out.print(pesan);
            hasil = input.nextLine().trim();

            if (hasil.isEmpty()) {
                System.out.println("Input tidak boleh kosong");
            }
        } while (hasil.isEmpty());

        return hasil;
    }

    public static int bacaAngka(String pesan) {
        while (true) {
            System.out.print(pesan);
            String teks = input.nextLine();

            try {
                return Integer.parseInt(teks);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang benar");
            }
        }
    }

    public static void main(String[] args) {
        Klinik klinik = new Klinik();

        klinik.tambahDokter(
                new Dokter("D01", "Dokter Andi", "Umum")
        );
        klinik.tambahDokter(
                new Dokter("D02", "Dokter Budi", "Anak")
        );

        int pilih;

        do {
            System.out.println("\n=== KLINIK RAWAT JALAN ===");
            System.out.println("1. Tambah pasien");
            System.out.println("2. Tambah dokter");
            System.out.println("3. Tampilkan pasien");
            System.out.println("4. Tampilkan dokter");
            System.out.println("5. Cari pasien");
            System.out.println("6. Buat antrean");
            System.out.println("7. Tampilkan antrean");
            System.out.println("8. Lihat antrean depan");
            System.out.println("9. Proses antrean");
            System.out.println("10. Riwayat kunjungan");
            System.out.println("11. Rekam medis");
            System.out.println("12. Ringkasan");
            System.out.println("0. Keluar");

            pilih = bacaAngka("Pilih menu: ");

            switch (pilih) {
                case 1:
                    String id = bacaTeks("ID pasien: ");
                    String nama = bacaTeks("Nama: ");
                    int umur = bacaAngka("Umur: ");
                    String alamat = bacaTeks("Alamat: ");

                    try {
                        Pasien p = new Pasien(id, nama, umur, alamat);

                        if (klinik.tambahPasien(p)) {
                            System.out.println("Pasien ditambahkan");
                        } else {
                            System.out.println("ID pasien sudah ada");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    String idDokter = bacaTeks("ID dokter: ");
                    String namaDokter = bacaTeks("Nama dokter: ");
                    String spesialis = bacaTeks("Spesialis: ");

                    Dokter d = new Dokter(
                            idDokter, namaDokter, spesialis
                    );

                    if (klinik.tambahDokter(d)) {
                        System.out.println("Dokter ditambahkan");
                    } else {
                        System.out.println("ID dokter sudah ada");
                    }
                    break;

                case 3:
                    klinik.tampilPasien();
                    break;

                case 4:
                    klinik.tampilDokter();
                    break;

                case 5:
                    String cari = bacaTeks("ID atau nama pasien: ");
                    Pasien hasil = klinik.cariPasien(cari);

                    if (hasil == null) {
                        System.out.println("Pasien tidak ditemukan");
                    } else {
                        hasil.tampil();
                    }
                    break;

                case 6:
                    String idP = bacaTeks("ID pasien: ");
                    String idD = bacaTeks("ID dokter: ");
                    klinik.buatAntrean(idP, idD);
                    break;

                case 7:
                    klinik.tampilAntrean();
                    break;

                case 8:
                    klinik.lihatDepanAntrean();
                    break;

                case 9:
                    String diagnosis = bacaTeks("Diagnosis: ");
                    String tindakan = bacaTeks("Tindakan: ");

                    klinik.prosesAntrean(diagnosis, tindakan);
                    break;

                case 10:
                    klinik.tampilKunjungan();
                    break;

                case 11:
                    klinik.tampilRekamMedis();
                    break;

                case 12:
                    klinik.ringkasan();
                    break;

                case 0:
                    System.out.println("Program selesai");
                    break;

                default:
                    System.out.println("Menu tidak tersedia");
            }

        } while (pilih != 0);
    }
}
