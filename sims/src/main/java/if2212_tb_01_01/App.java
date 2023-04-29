/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package if2212_tb_01_01;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import if2212_tb_01_01.entities.*;
import if2212_tb_01_01.objects.*;
import if2212_tb_01_01.utils.*;

public class App{
    class MainMenu {
        public MainMenu() {
        }
        public void show(boolean started) {
            if(!started){
                System.out.println("+---------------+");
                System.out.println("| List Menu :   |");
                System.out.println("| 1. Start Game |");
                System.out.println("| 2. Load Game  |");
                System.out.println("| 3. Help       |");
                System.out.println("| 4. Exit       |");
                System.out.println("+---------------+\n");

            }
            else{
                System.out.println("+--------------------------+");
                System.out.println("| List Menu :              |");
                System.out.println("| 1. Save Game             |");
                System.out.println("| 2. View Sim Info         |");
                System.out.println("| 3. View Current Location |");
                System.out.println("| 4. View Inventory        |");
                System.out.println("| 5. Upgrade House         |");
                System.out.println("| 6. Move Room             |");
                System.out.println("| 7. Edit Room             |");
                System.out.println("| 8. Add Sim               |");
                System.out.println("| 9. Change Sim            |");
                System.out.println("| 10. List Object          |");
                System.out.println("| 11. Go To Object         |");
                System.out.println("| 12. Action               |");
                System.out.println("| 13. Exit                 |");
                System.out.println("+--------------------------+");
            }
        }

    }
    private static volatile boolean running = true;
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        App app = new App();
        MainMenu mainMenu = app.new MainMenu();
        boolean startgame = false;
        boolean loadgame = false;
        boolean endedgame = false;
        boolean isCanAddSim = true;
        String command;
        World world = new World(64,64);
        WorldClock worldClock = new WorldClock(world);
        Thread clockThread = new Thread(worldClock);
        clockThread.start();
        Integer indeksActiveSim = 0;
        System.out.println("Selamat datang di Sim-Plicity!");
        while(!startgame && !loadgame && !endedgame){
            mainMenu.show(startgame);
            System.out.print("Masukkan command: ");
            command = scanner.nextLine().toUpperCase();
            
            //validasi command
            if (command.equals("START GAME")){
                startgame = true;
            }
            else if(command.equals("LOAD GAME")){
                loadgame = true;
            }

            else if (command.equals("EXIT")){
                endedgame = true;
                System.out.println("Sampai jumpa kembali! \n");
            }
            else if(command.equals("HELP")){
                System.out.println("GAME K*NT*L!!");
            }
            else{
                System.out.println("Command tidak valid!! \n");
            }
        }

        if(startgame){
            //memuat world
            System.out.println("\nMemuat World....");
            Point posisiRuangan;
            Point posisiRumah;

            //Meminta nama sim
            boolean nameValid = false;
            while(!nameValid){
                System.out.print("Silakan masukkan nama lengkap: ");
                String name = scanner.nextLine();
                if(!name.isBlank()){
                    nameValid = true;
                    //Meminta posisi Rumah yang ingin dibangun
                    boolean posisiRumahValid = false;
                    while(!posisiRumahValid){
                        System.out.print("Masukkan lokasi rumah yang diinginkan dengan format x,y (dari 0,0 sampai 64,64): ");
                        command = scanner.nextLine();
                        posisiRumahValid = InputChecker.isPointRumah(command);
                        if(posisiRumahValid){
                            posisiRumah = Point.makePoint(command);
                            //Meminta nama Ruangan yang ingin dibangun
                            boolean ruanganValid = false;
                            while(!ruanganValid){
                                System.out.print("Masukkan nama ruangan: ");
                                String namaRuangan = scanner.nextLine();
                                if(!namaRuangan.isBlank()){
                                    ruanganValid = true;
                                    //PosisiRuangan random aja kali yak gw bikinnya dari 0 sampai 6
                                    posisiRuangan = Point.makeRandomizePoint();
                                    System.out.println("\nMemuat Rumah....");
                                    world.addRumah(new Point(posisiRumah),name,namaRuangan,new Point(posisiRuangan));
                                    world.addSim(new Sim(new Kesejahteraan(80,80,80,80), 100, name, world.getLastRumah(), world.getLastRumah().getPosisi(), world.getLastRumah().getDaftarRuangan().get(0).getPosisi()));
                                    indeksActiveSim = world.getJumlahSim() - 1;
                                    System.out.println("Selamat datang, " + name + "!");
                                }
                                else{
                                    System.out.println("Nama ruangan tidak valid");
                                }
                            }
                        }
                        //else untuk posisi rumah tidak valid sudah pake exception
                    }
                }
                else{
                    System.out.println("Nama tidak valid!");
                }
            }
            // memulai thread counter waktu
        }

        while(!endedgame){
            mainMenu.show(startgame);
            System.out.print("Masukkan command: ");
            command = scanner.nextLine().toUpperCase();

            //Menu nomor 2 (AMAN)
            if(command.equals("VIEW SIM INFO")){
                world.getSim(indeksActiveSim).viewInfo();
            }

            //Menu nomor 3 (AMAN)
            else if(command.equals("VIEW CURRENT LOCATION")){
                Rumah currentLocationRumah = world.getRumah(world.getSim(indeksActiveSim).getPosisiRumah());
                world.getSim(indeksActiveSim).viewLokasi(currentLocationRumah);
            }

            //Menu nomor 4 (AMAN)
            else if (command.equals("VIEW INVENTORY")){
                world.getSim(indeksActiveSim).viewInventory();
            }

            // Menu nomor 5 (Harusnya udh aman sih tinggal thread waktunya aja)
            else if (command.equals("UPGRADE HOUSE")){
                if(world.getSim(indeksActiveSim).getUang() < 1500){
                    System.out.println("Uang tidak cukup! Upgrade house gagal dilakukan");
                }
                else{
                    Point posisiRumah = world.getSim(indeksActiveSim).getPosisiRumah();
                    Point posisiRuanganAcuan;
                    Point posisiRuanganBaru;
                    world.getRumah(posisiRumah).printDaftarRuangan();
                    boolean inputValid = false;
                    boolean namaRuanganValid = false;
                    String namaRuangan;
                    Integer nomorRuangan;
                    while(!inputValid){
                        System.out.println("Pilih nomor ruangan yang ingin dijadikan acuan penambahan: ");
                        command = scanner.nextLine();
                        nomorRuangan = InputChecker.toAngka(command);
                        if(!nomorRuangan.equals(-999)){
                            try{
                                posisiRuanganAcuan = world.getRumah(posisiRumah).getRuangan(nomorRuangan - 1).getPosisi();
                                inputValid = true;
                                while(!namaRuanganValid){
                                    System.out.print("Masukan nama ruangan: ");
                                    command = scanner.nextLine();
                                    if(!world.getRumah(posisiRumah).adaRuangan(command)){
                                        namaRuangan = command;
                                        namaRuanganValid = true;
                                        System.out.print("Pilih posisi ruangan baru (atas/bawah/kiri/kanan): ");
                                        command = scanner.nextLine().toUpperCase();
                                        if (command.equals("ATAS")) {
                                            if(!world.getRumah(posisiRumah).adaRuangan(new Point(posisiRuanganAcuan.getX(), posisiRuanganAcuan.getY() + 1))){
                                                posisiRuanganBaru = new Point(posisiRuanganAcuan.getX(),posisiRuanganAcuan.getY()+1);
                                                world.getRumah(posisiRumah).upgradeRumah(namaRuangan, posisiRuanganBaru);
                                                world.getSim(indeksActiveSim).setUang(world.getSim(indeksActiveSim).getUang() - 1500);  
                                                System.out.println("Upgrade House berhasil dilakukan! Silakan menunggu 18 menit"); 
                                            }
                                            else{
                                                System.out.println("Posisi yang dipilih sudah terdapat ruangan! Upgrade House gagal dilakukan");
                                            }
                                        }
                                        else if (command.equals("BAWAH")) {
                                            if(!world.getRumah(posisiRumah).adaRuangan(new Point(posisiRuanganAcuan.getX(), posisiRuanganAcuan.getY() - 1))){
                                                posisiRuanganBaru = new Point(posisiRuanganAcuan.getX(),posisiRuanganAcuan.getY()-1);
                                                world.getRumah(posisiRumah).upgradeRumah(namaRuangan, posisiRuanganBaru);
                                                world.getSim(indeksActiveSim).setUang(world.getSim(indeksActiveSim).getUang() - 1500);
                                                System.out.println("Upgrade House berhasil dilakukan! Silakan menunggu 18 menit");
                                            }
                                            else{
                                                System.out.println("Posisi yang dipilih sudah terdapat ruangan! Upgrade House gagal dilakukan");
                                            }
                                        }
                                        else if (command.equals("KIRI")) {
                                            if(!world.getRumah(posisiRumah).adaRuangan(new Point(posisiRuanganAcuan.getX() - 1, posisiRuanganAcuan.getY()))){
                                                posisiRuanganBaru = new Point(posisiRuanganAcuan.getX() - 1,posisiRuanganAcuan.getY());
                                                world.getRumah(posisiRumah).upgradeRumah(namaRuangan, posisiRuanganBaru);
                                                world.getSim(indeksActiveSim).setUang(world.getSim(indeksActiveSim).getUang() - 1500);
                                                System.out.println("Upgrade House berhasil dilakukan! Silakan menunggu 18 menit");
                                            }
                                            else{
                                                System.out.println("Posisi yang dipilih sudah terdapat ruangan! Upgrade House gagal dilakukan");
                                            }
                                        }
                                        else if (command.equals("KANAN")) {
                                            if(!world.getRumah(posisiRumah).adaRuangan(new Point(posisiRuanganAcuan.getX() + 1, posisiRuanganAcuan.getY()))){
                                                posisiRuanganBaru = new Point(posisiRuanganAcuan.getX() + 1,posisiRuanganAcuan.getY());
                                                world.getRumah(posisiRumah).upgradeRumah(namaRuangan, posisiRuanganBaru);
                                                world.getSim(indeksActiveSim).setUang(world.getSim(indeksActiveSim).getUang() - 1500);
                                                System.out.println("Upgrade House berhasil dilakukan! Silakan menunggu 18 menit");
                                            }
                                            else{
                                                System.out.println("Posisi yang dipilih sudah terdapat ruangan! Upgrade House gagal dilakukan");
                                            }
                                        }
                                        else{
                                            System.out.println("Input tidak valid! Upgrade house gagal dilakukan");
                                        }
                                    }
                                    else{
                                        System.out.println("Nama ruangan sudah ada! silahkan masukkan nama lain");
                                    }  
                                }
                            }
                            catch(IndexOutOfBoundsException e){
                                System.err.println("Input tidak valid! (Hint: input < 1 atau melebihi total ruangan yang terdapat di dalam rumah)");
                            }
                        }
                        //else sudah lewat exception
                    }
                }
                
            }

            //Menu nomor 6 (harusnya aman)
            else if (command.equals("MOVE ROOM")){
                Point posisiRumah = world.getSim(indeksActiveSim).getPosisiRumah();
                world.getRumah(posisiRumah).printDaftarRuangan();
                boolean inputValid = false;
                Integer nomorRuangan;
                Point posisiRuanganTujuan;
                String namaRuangan;
                while(!inputValid){
                    System.out.print("Pilih nomor ruangan yang ingin dituju: ");
                    command = scanner.nextLine();
                    nomorRuangan = InputChecker.toAngka(command);
                    if(!nomorRuangan.equals(-999)){
                        try{
                            posisiRuanganTujuan = world.getRumah(posisiRumah).getRuangan(nomorRuangan - 1).getPosisi();
                            namaRuangan = world.getRumah(posisiRumah).getRuangan(nomorRuangan - 1).getNama();
                            inputValid = true;
                            world.getSim(indeksActiveSim).berpindahRuangan(posisiRuanganTujuan, namaRuangan);
                        }
                        catch(IndexOutOfBoundsException e){
                            System.err.println("Input tidak valid! (Hint: input < 1 atau melebihi total ruangan yang terdapat di dalam rumah)");
                        }    
                    }
                }
            }

            //Menu nomor 7 (Belum pemindahan barang, sama ngeset waktu pengiriman barang)
            else if(command.equals("EDIT ROOM")){
                System.out.println("Opsi kegiatan yang dapat dilakukan:");
                System.out.println("1.Beli barang");
                System.out.println("2.Pemindahan barang");
                System.out.print("Pilih kegiatan yang ingin dilakukan: ");
                command = scanner.nextLine().toUpperCase();
                if(command.equals("BELI BARANG") || command.equals("PEMINDAHAN BARANG")){
                    String kegiatan = command;
                    if(kegiatan.equals("BELI BARANG")){
                        System.out.println("Kategori barang yang dapat dibeli:");
                        System.out.println("1.Makanan");
                        System.out.println("2.Furnitur");
                        System.out.print("Pilih kategori barang yang ingin dibeli: ");
                        command = scanner.nextLine().toUpperCase();
                        if(command.equals("MAKANAN") || command.equals("FURNITUR")){
                            String kategori = command;
                            if(kategori.equals("MAKANAN")){
                                Makanan.printListMakanan();
                                System.out.println("Pilih makanan yang ingin dibeli: ");
                            }
                            else if(kategori.equals("FURNITUR")){
                                Furnitur.printListFurnitur();
                                System.out.println("Pilih furnitur yang ingin dibeli: ");
                            }
                            command = scanner.nextLine().toUpperCase();
                            world.getSim(indeksActiveSim).beliBarang(command, kategori);
                        }
                        else{
                            System.out.println("input kategori tidak valid!");
                        }
                    }
                    else if(kegiatan.equals("PEMINDAHAN BARANG")){

                    }
                }
                else{
                    System.out.println("Input kegiatan tidak valid!\n");
                }
            }

            // Menu nomor 8
            else if(command.equals("ADD SIM")){
                if (isCanAddSim){
                    Point posisiRuangan;
                    Point posisiRumah;

                    //Meminta nama sim
                    boolean nameValid = false;
                    while(!nameValid){
                        System.out.print("Silakan masukkan nama lengkap: ");
                        String name = scanner.nextLine();
                        if(!name.isBlank()){
                            int i = 0;
                            boolean namaSimFound = false;
                            while(!namaSimFound && i < world.getJumlahSim()){
                                if(world.getSim(i).getNamaLengkap().equals(name)){
                                    namaSimFound = true;
                                }
                                else{
                                    i++;
                                }
                            }
                            if(!namaSimFound){
                                nameValid = true;
                                //Meminta posisi Rumah yang ingin dibangun
                                boolean posisiRumahValid = false;
                                boolean posisiRumahTerisi = true;
                                while(!posisiRumahValid || posisiRumahTerisi){
                                    System.out.print("Masukkan lokasi rumah yang diinginkan dengan format x,y (dari 0,0 sampai 64,64): ");
                                    command = scanner.nextLine();
                                    posisiRumahValid = InputChecker.isPointRumah(command);
                                    if(posisiRumahValid){
                                        posisiRumah = Point.makePoint(command);
                                        if(!world.isPosisiTerisi(posisiRumah)){
                                            posisiRumahTerisi = false;
                                            //Meminta nama ruangan yang ingin dibangun
                                            boolean ruanganValid = false;
                                            while(!ruanganValid){
                                                System.out.print("Masukkan nama ruangan: ");
                                                String namaRuangan = scanner.nextLine();
                                                if(!namaRuangan.isBlank()){
                                                    ruanganValid = true;
                                                    //PosisiRuangan random aja kali yak gw bikinnya dari 0 sampai 6
                                                    posisiRuangan = Point.makeRandomizePoint();
                                                    System.out.println("\nMemuat Rumah....");
                                                    System.out.println("Sim "+ name + "berhasil dibuat! ");
                                                    world.addRumah(new Point(posisiRumah),name,namaRuangan,new Point(posisiRuangan));
                                                    world.addSim(new Sim(new Kesejahteraan(80,80,80,80), 100, name, world.getLastRumah(), world.getLastRumah().getPosisi(), world.getLastRumah().getDaftarRuangan().get(0).getPosisi()));
                                                    System.out.println("Selamat datang, " + name + "!");
                                                    isCanAddSim = false;
                                                }
                                                else{
                                                    System.out.println("Nama ruangan tidak valid");
                                                }
                                            }
                                        }
                                        else{
                                            System.out.println("Masukan tidak valid! (Hint: Posisi rumah sudah terisi, silakan pilih posisi lain)");
                                        }

                                        //Meminta nama Ruangan yang ingin dibangun
                                        
                                    }
                                    //else untuk posisi rumah tidak valid sudah pake exception
                                }
                            }
                            else{
                                System.out.println("Masukan tidak valid! (Hint: Input nama sudah dipakai sim lain, silakan pilih nama yang lain)");
                            } 
                        }
                        else{
                            System.out.println("Masukan tidak valid! (Hint: Input anda kosong)");
                        }
                    }
                }
                else{
                    System.out.println("Kesempatan menambah sim sudah digunakan, silakan menunggu hingga esok hari");
                }
            }

            //Menu nomor 9
            else if (command.equals("CHANGE SIM")){
                if (world.getJumlahSim() > 1){
                    System.out.println("Daftar Sim yang dapat dipilih:");
                    for (int i = 0; i < world.getJumlahSim() ; i++){
                        System.out.println((i+1) + ". " + world.getSim(i).getNamaLengkap());
                    }
                    System.out.print("Pilih nomor Sim yang ingin dipilih: ");
                    command = scanner.nextLine();
                    Integer pilihan = InputChecker.toAngka(command); //exception
                    if(!pilihan.equals(-999)){
                        if (pilihan >= 1 && pilihan <= world.getJumlahSim()){
                            indeksActiveSim = pilihan-1;
                            System.out.println("Berhasil mengganti sim active menjadi " + world.getSim(indeksActiveSim).getNamaLengkap());
                        } 
                    }
                    else{
                        System.out.println("Input tidak valid!");
                    }
                }
                else{
                    System.out.println("Tidak ada Sim lain yang dapat dipilih!");
                }
            } 
            
            //Menu nomor 10 (Harusnya udh aman)
            else if (command.equals("LIST OBJECT")){
                Point lokasiRumah = world.getSim(indeksActiveSim).getPosisiRumah();
                Point lokasiRuangan = world.getSim(indeksActiveSim).getPosisiRuangan();
                Ruangan currentRuangan = world.getRumah(lokasiRumah).getRuangan(lokasiRuangan);
                System.out.println("Kamu sedang berada di ruangan " + currentRuangan.getNama()+ "!");
                if(currentRuangan.getDaftarObjek().isEmpty()){
                    System.out.println("Tidak terdapat objek di dalam ruanganmu:");
                }
                else{
                    System.out.println("Berikut adalah daftar objek yang ada di ruanganmu:");
                    int i=1;
                    for (Furnitur furnitur : currentRuangan.getDaftarObjek()){
                        System.out.println(i + ". " + furnitur.getNama()+" (lokasi: "+ furnitur.getPosisi() +")");
                        i+=1;
                    }
                }
            } 
            
            //Menu nomor 11 (Harusnya udh aman)
            else if (command.equals("GO TO OBJECT")){
                final int idx = indeksActiveSim;
                Point lokasiRumah = world.getSim(indeksActiveSim).getPosisiRumah();
                Point lokasiRuangan = world.getSim(indeksActiveSim).getPosisiRuangan();
                Ruangan currentRuangan = world.getRumah(lokasiRumah).getRuangan(lokasiRuangan);
                System.out.println("Kamu sedang berada di ruangan " + currentRuangan.getNama()+ "!");
                if(currentRuangan.getDaftarObjek().isEmpty()){
                    System.out.println("Ruangan kosong! tidak ada objek yang dapat dikunjungi");
                }
                else{
                    System.out.println("Berikut adalah daftar objek yang dapat dikunjungi");
                    int i = 1;
                    for (Furnitur furnitur : world.getRumah(lokasiRumah).getRuangan(lokasiRuangan).getDaftarObjek()) {
                        System.out.println(i + ". " + furnitur.getNama()+" (lokasi: "+ furnitur.getPosisi() +")");
                        i++;
                    }
                    Furnitur objek;
                    boolean inputValid = false;
                    while(!inputValid){
                        System.out.println("Pilih nomor objek yang ingin dikunjungi: ");
                        command = scanner.nextLine();
                        try{
                            objek = world.getRumah(lokasiRumah).getRuangan(lokasiRuangan).getObjek(Integer.parseInt(command) - 1);
                            if(Integer.parseInt(command) >= 1 && Integer.parseInt(command) < i){
                                inputValid = true;
                                world.getSim(indeksActiveSim).setPosisiObjek(objek.getPosisi());
                                System.out.println(world.getSim(indeksActiveSim).getNamaLengkap() +" berhasil pindah ke objek "+ objek.getNama());
                                if (objek.getKategori().equals("peralatan")){
                                    Furnitur furnitur = (Furnitur) objek;
                                    if (furnitur.getAksi().equals("TIDUR")){
                                        System.out.println("Apakah Anda ingin tidur? (Y/N)");
                                        String pilihanTidur = scanner.nextLine().toUpperCase();
                                        if (pilihanTidur.equals("Y")){
                                            executorService.execute(() -> {
                                                world.getSim(idx).tidur();
                                                worldClock.setWorld(world);
                                            });
                                        }
                                    }
                                    else if (furnitur.getAksi().equals("BUANG AIR")){
                                        System.out.println("Apakah Anda ingin buang air? (Y/N)");
                                        String pilihanBuangAir = scanner.nextLine().toUpperCase();
                                        if (pilihanBuangAir.equals("Y")){
                                            world.getSim(indeksActiveSim).buangAir();
                                        }
                                    } else if(furnitur.getAksi().equals("MASAK")){
                                        System.out.println("Apakah Anda ingin memasak? (Y/N)");
                                        String pilihanMasak = scanner.nextLine().toUpperCase();
                                        if (pilihanMasak.equals("Y")){
                                            System.out.println("Masukkan nama masakan yang ingin dibuat: ");
                                            // String namaMasakan = scanner.next();
                                            // this.masak();
                                        }
                                    } else if (furnitur.getAksi().equals("MAKAN")){
                                        System.out.println("Apakah Anda ingin makan? (Y/N)");
                                        String pilihanMakan = scanner.nextLine().toUpperCase();
                                        if (pilihanMakan.equals("Y")){
                                            world.getSim(indeksActiveSim).makan();
                                        }
                                    } else if(furnitur.getAksi().equals("MELIHAT WAKTU")){
                                        System.out.println("Apakah Anda ingin melihat waktu? (Y/N)");
                                        String pilihanLihatWaktu = scanner.nextLine().toUpperCase();
                                        if (pilihanLihatWaktu.equals("Y")){
                                            world.getSim(indeksActiveSim).melihatWaktu();
                                        }
                                    } else if(furnitur.getAksi().equals("MELUKIS")){
                                        System.out.println("Apakah Anda ingin melukis? (Y/N)");
                                        String pilihanMelukis = scanner.nextLine().toUpperCase();
                                        if (pilihanMelukis.equals("Y")){
                                            world.getSim(indeksActiveSim).melukis();
                                        }
                                    } else if(furnitur.getAksi().equals("BERMAIN MUSIK")){
                                        System.out.println("Apakah Anda ingin bermain musik? (Y/N)");
                                        String pilihanMusik = scanner.nextLine().toUpperCase();
                                        if (pilihanMusik.equals("Y")){
                                            world.getSim(indeksActiveSim).bermainMusik();
                                        }
                                    } else if(furnitur.getAksi().equals("MANDI")){
                                        System.out.println("Apakah Anda ingin mandi? (Y/N)");
                                        String pilihanMandi = scanner.nextLine().toUpperCase();
                                        if (pilihanMandi.equals("Y")){
                                            world.getSim(indeksActiveSim).mandi();
                                        }
                                    } else if(furnitur.getAksi().equals("MEMBERSIHKAN RUMAH")){
                                        System.out.println("Apakah Anda ingin membersihkan rumah? (Y/N)");
                                        String pilihanBersihRumah = scanner.nextLine().toUpperCase();
                                        if (pilihanBersihRumah.equals("Y")){
                                            world.getSim(indeksActiveSim).membersihkanRumah();
                                        }
                                    } else if(furnitur.getAksi().equals("PROYEKAN")){
                                        System.out.println("Apakah Anda ingin mengerjakan proyek? (Y/N)");
                                        String pilihanProyekan = scanner.nextLine().toUpperCase();
                                        if (pilihanProyekan.equals("Y")){
                                            world.getSim(indeksActiveSim).proyekan();
                                        }
                                    } else {
                                        System.out.println("Aksi tidak tersedia");
                                    }
                                }
                            }
                        }
                        catch(IndexOutOfBoundsException e){
                            System.err.println("Input tidak valid! (Hint : input < 1 atau melebihi total objek yang terdapat di dalam ruangan)");
                        }
                        catch(NumberFormatException e){
                            System.err.println("Input tidak valid! (Hint : input harus berupa angka) ");
                        }
                    }
                }
            } 
            
            //Menu nomor 12 (menu dajjal) <-- real
            else if (command.equals("ACTION")){
                System.out.println("\nBerikut adalah daftar aksi yang bisa kamu lakukan: ");
                System.out.println("1. Kerja");
                System.out.println("2. Olahraga");
                System.out.println("3. Berkunjung");
                System.out.println("4. Yoga");
                System.out.println("5. Berdoa");
                System.out.println("6. Melihat inventory");
                System.out.println("7. Beli barang\n");
                System.out.print("Pilih nomor aksi yang ingin dilakukan: ");
                command = scanner.nextLine().toUpperCase();
                if (command.equals("KERJA")){
                    world.getSim(indeksActiveSim).kerja();
                } else if (command.equals("OLAHRAGA")){
                    world.getSim(indeksActiveSim).olahraga();
                } else if (command.equals("BERKUNJUNG")){
                    world.getSim(indeksActiveSim).berkunjung();
                } else if (command.equals("YOGA")){
                    world.getSim(indeksActiveSim).yoga();
                } else if (command.equals("BERDOA")){
                    world.getSim(indeksActiveSim).berdoa();
                } else if (command.equals("MELIHAT INVENTORY")){
                    world.getSim(indeksActiveSim).viewInventory();
                } else if (command.equals("BELI BARANG")){
                    System.out.println("Kategori barang yang dapat dibeli:");
                    System.out.println("1.Makanan");
                    System.out.println("2.Furnitur");
                    System.out.print("Pilih kategori barang yang ingin dibeli: ");
                    command = scanner.nextLine().toUpperCase();
                    if(command.equals("MAKANAN") || command.equals("FURNITUR")){
                        String kategori = command;
                        if(kategori.equals("MAKANAN")){
                            Makanan.printListMakanan();
                            System.out.println("Pilih makanan yang ingin dibeli: ");
                        }
                        else if(kategori.equals("FURNITUR")){
                            Furnitur.printListFurnitur();
                            System.out.println("Pilih furnitur yang ingin dibeli: ");
                        }
                        command = scanner.nextLine().toUpperCase();
                        world.getSim(indeksActiveSim).beliBarang(command, kategori);
                    }
                } 
            } 
            
            //Menu nomor 13 (aman, paling kalo mau tambahin fitur save sabi ditanya mau disave dulu gak)
            else if (command.equals("EXIT")){
                endedgame = true;
                System.out.println("Sampai jumpa kembali! \n");
            } else {
                System.out.println("Input tidak valid! Silahkan coba lagi.");
            }
            worldClock.setWorld(world); // biar terupdate trs
        }
        scanner.close();
    }   
}
