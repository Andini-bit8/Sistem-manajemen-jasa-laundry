//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

fun main(){
    val url = "jdbc:mysql://localhost:3306/sistem_manajemen_jasa_laundry"
    val user = "root"
    val password= ""


    try
    {
        val connection: Connection = DriverManager.getConnection(url, user, password)
        println("Koneksi berhasil")
        println("==== Selamat Datang di UAS Me ===" )
        println("\n=== Sistem Manajemen Jasa Laundry ====")
        if (login(connection)) {

            var menuAktif = true
            while (menuAktif) {
                println("\nMenu Utama:")
                println("1. mengelola data transaksi pakaian")
                println("2. Mengelola data pendapatan")
                println("3. Mengelola data pengeluaran")
                println("4. Mengelola data konsumen")
                println("5. Logout")
                print("Pilih menu: ")

                val pilihan = readLine()?.toIntOrNull()
                when (pilihan) {
                    1 -> mengelola_data_transaksi_pakaian(connection)
                    2 -> mengelola_data_pendapatan(connection)
                    3 -> mengelola_data_pengeluaran(connection)
                    4 -> mengelola_data_konsumen(connection)
                    5 -> {
                        menuAktif = false
                        println("Keluar.")
                    }

                    else -> println("Pilihan tidak valid, coba lagi.")
                }
            }
        } else {
            println("Login gagal")
        }

         connection.close()
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
fun login(connection: Connection): Boolean {
    while (true) {
        print("Masukkan id_admin: ")
        val id_admin = readLine()?.toIntOrNull()

        if (id_admin == null) {
            println("ID Admin Salah. Silakan coba lagi.")
            continue
        }

        print("Masukkan username: ")
        val username = readLine()
        print("Masukkan password: ")
        val password = readLine()

        val query = "SELECT * FROM admin WHERE id_admin = ? AND username = ? AND password = ?"
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, id_admin)
        preparedStatement.setString(2, username)
        preparedStatement.setString(3, password)

        val resultSet = preparedStatement.executeQuery()
        val loginBerhasil = resultSet.next()

        resultSet.close()
        preparedStatement.close()

        if (loginBerhasil) {
            println("Login berhasil!")
            return true
        } else {
            println("Login gagal. Silakan coba lagi.")
        }
    }
}
fun mengelola_data_pendapatan(connection: Connection) {
    while (true) {
        println("=== Mengelola Data Pendapatan ===")
        println("1. Tambah Data Pendapatan")
        println("2. Tampilkan Semua Data Pendapatan")
        println("3. Ubah Data Pendapatan")
        println("4. Hapus Data Pendapatan")
        println("5. Kembali ke Menu Utama")
        print("Pilih menu: ")

        when (readLine()?.toIntOrNull()) {
            1 -> tambah_data_pendapatan(connection)
            2 -> tampilkan_data_pendapatan(connection)
            3 -> ubah_data_pendapatan(connection)
            4 -> hapus_data_pendapatan(connection)
            5 -> break
            else -> println("Pilihan tidak valid. Silakan coba lagi.")
        }
    }
}

fun tambah_data_pendapatan(connection: Connection) {
    try {
        print("Masukkan ID pendapatan baru: ")
        val id_pendapatan = readLine()?.toIntOrNull()

        if (id_pendapatan != null) {
            print("Masukkan tanggal baru (YYYY-MM-DD): ")
            val tanggal_pendapatan = readLine()
            print("Masukkan deskripsi baru: ")
            val deskripsi = readLine()
            print("Masukkan jumlah baru: ")
            val jumlah_pembayaran = readLine()?.toDoubleOrNull()

            if (tanggal_pendapatan != null && deskripsi != null && jumlah_pembayaran != null) {
                val query = "INSERT INTO mengelola_data_pendapatan (id_pendapatan, tanggal_pendapatan, deskripsi, jumlah_pembayaran) values (?,?,?,?)"
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, id_pendapatan)
                preparedStatement.setString(2, tanggal_pendapatan)
                preparedStatement.setString(3, deskripsi)
                preparedStatement.setDouble(4, jumlah_pembayaran)
                preparedStatement.executeUpdate()
                println("Data pendapatan berhasil ditambah.")
            } else {
                println("Input tidak valid. Silakan coba lagi.")
            }
        } else {
            println("ID tidak valid. Silakan coba lagi.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun tampilkan_data_pendapatan(connection: Connection) {
    try {
        val query = "SELECT * FROM mengelola_data_pendapatan"
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        println("=== Data Pendapatan ===")
        while (resultSet.next()) {
            val id_pendapatan = resultSet.getInt("id_pendapatan")
            val tanggal_pendapatan = resultSet.getDate("tanggal_pendapatan")
            val deskripsi = resultSet.getString("deskripsi")
            val jumlah_pembayaran = resultSet.getInt("jumlah_pembayaran")
            println("ID: $id_pendapatan, Tanggal: $tanggal_pendapatan, Deskripsi: $deskripsi, Jumlah: $jumlah_pembayaran")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ubah_data_pendapatan(connection: Connection) {
    try {
        print("Masukkan ID pendapatan yang ingin diubah: ")
        val id_pendapatan = readLine()?.toIntOrNull()

        if (id_pendapatan != null) {
            print("Masukkan tanggal (YYYY-MM-DD): ")
            val tanggal_pendapatan = readLine()
            print("Masukkan deskripsi: ")
            val deskripsi = readLine()
            print("Masukkan jumlah pendapatan: ")
            val jumlah_pembayaran = readLine()?.toDoubleOrNull()

            if (tanggal_pendapatan != null && deskripsi != null && jumlah_pembayaran != null) {
                val query = "UPDATE mengelola_data_pendapatan SET tanggal_pendapatan = ?, deskripsi = ?, jumlah_pembayaran= ? WHERE id_pendapatan = ?"
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, tanggal_pendapatan)
                preparedStatement.setString(2, deskripsi)
                preparedStatement.setDouble(3, jumlah_pembayaran)
                preparedStatement.setInt(4, id_pendapatan)
                preparedStatement.executeUpdate()
                println("Data pendapatan berhasil diubah.")
            } else {
                println("Input tidak valid. Silakan coba lagi.")
            }
        } else {
            println("ID tidak valid. Silakan coba lagi.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun hapus_data_pendapatan(connection: Connection) {
    try {
        print("Masukkan ID pendapatan yang ingin dihapus: ")
        val id_pendapatan= readLine()?.toIntOrNull()

        if (id_pendapatan != null) {
            val query = "DELETE FROM mengelola_data_pendapatan WHERE id_pendapatan= ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, id_pendapatan)
            preparedStatement.executeUpdate()
            println("Data pendapatan berhasil dihapus.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



fun mengelola_data_pengeluaran(connection: Connection) {
    while (true) {
        println("=== Mengelola Data Pengeluaran ===")
        println("1. Tambah Data Pengeluaran")
        println("2. Tampilkan Semua Data Pengeluaran")
        println("3. Ubah Data Pengeluaran")
        println("4. Hapus Data Pengeluaran")
        println("5. Kembali ke Menu Utama")
        print("Pilih menu: ")

        when (readLine()?.toIntOrNull()) {
            1 -> tambah_data_pengeluaran(connection)
            2 -> tampilkan_data_pengeluaran(connection)
            3 -> ubah_data_pengeluaran(connection)
            4 -> hapus_data_pengeluaran(connection)
            5 -> break
            else -> println("Pilihan tidak valid. Silakan coba lagi.")
        }
    }
}

fun tambah_data_pengeluaran(connection: Connection) {
    try {
        print("Masukkan ID pengeluaran baru: ")
        val id_pengeluaran = readLine()?.toIntOrNull()

        if (id_pengeluaran != null) {
            print("Masukkan tanggal baru (YYYY-MM-DD): ")
            val tanggal_pengeluaran = readLine()
            print("Masukkan deskripsi baru: ")
            val deskripsi = readLine()
            print("Masukkan jumlah pengeluaran baru: ")
            val jumlah_pengeluaran = readLine()?.toDoubleOrNull()

            if (tanggal_pengeluaran != null && deskripsi != null && jumlah_pengeluaran != null) {
                val query = "INSERT INTO mengelola_data_pengeluaran (id_pengeluaran, tanggal_pengeluaran, deskripsi, jumlah_pengeluaran) values (?,?,?,?)"
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, id_pengeluaran)
                preparedStatement.setString(2, tanggal_pengeluaran)
                preparedStatement.setString(3, deskripsi)
                preparedStatement.setDouble(4, jumlah_pengeluaran)
                preparedStatement.executeUpdate()
                println("Data pengeluaran berhasil ditambah.")
            } else {
                println("Input tidak valid. Silakan coba lagi.")
            }
        } else {
            println("ID tidak valid. Silakan coba lagi.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun tampilkan_data_pengeluaran(connection: Connection) {
    try {
        val query = "SELECT * FROM mengelola_data_pengeluaran"
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        println("=== Data Pengeluaran ===")
        while (resultSet.next()) {
            val id_pengeluaran = resultSet.getInt("id_pengeluaran")
            val tanggal_pengeluaran = resultSet.getDate("tanggal_pengeluaran")
            val deskripsi = resultSet.getString("deskripsi")
            val jumlah_pengeluaran = resultSet.getInt("jumlah_pengeluaran")
            println("ID: $id_pengeluaran, Tanggal: $tanggal_pengeluaran Deskripsi: $deskripsi, Jumlah: $jumlah_pengeluaran")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun ubah_data_pengeluaran(connection: Connection) {
    try {
        print("Masukkan ID pengeluaran yang ingin diubah: ")
        val id_pengeluaran = readLine()?.toIntOrNull()

        if (id_pengeluaran != null) {
            print("Masukkan tanggal pengeluaran yang ingin diubah (YYYY-MM-DD): ")
            val tanggal_pengeluaran = readLine()
            print("Masukkan deskripsi : ")
            val deskripsi = readLine()
            print("Masukkan jumlah pengeluaran: ")
            val jumlah_pengeluaran = readLine()?.toDoubleOrNull()

            if (tanggal_pengeluaran != null && deskripsi != null && jumlah_pengeluaran != null) {
                val query = "UPDATE mengelola_data_pengeluaran SET tanggal_pengeluaran = ?, deskripsi = ?, jumlah_pengeluaran= ? WHERE id_pengeluaran = ?"
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, tanggal_pengeluaran)
                preparedStatement.setString(2, deskripsi)
                preparedStatement.setDouble(3, jumlah_pengeluaran)
                preparedStatement.setInt(4, id_pengeluaran)
                preparedStatement.executeUpdate()
                println("Data pendapatan berhasil diubah.")
            } else {
                println("Input tidak valid. Silakan coba lagi.")
            }
        } else {
            println("ID tidak valid. Silakan coba lagi.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun hapus_data_pengeluaran(connection: Connection) {
    try {
        print("Masukkan ID pengeluaran yang ingin dihapus: ")
        val id_pengeluaran= readLine()?.toIntOrNull()

        if (id_pengeluaran != null) {
            val query = "DELETE FROM mengelola_data_pengeluaran WHERE id_pengeluaran= ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, id_pengeluaran)
            preparedStatement.executeUpdate()
            println("Data pengeluaran berhasil dihapus.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun mengelola_data_transaksi_pakaian(connection: Connection) {
    while (true) {
        println("=== Mengelola Data Transaksi Pakaian===")
        println("1. Tambah Data Transaksi pakaian ")
        println("2. Tampilkan Semua Data Transaksi pakaian")
        println("3. Ubah Data Transaksi")
        println("4. Hapus Data Transaksi")
        println("5. Kembali ke Menu Utama")
        print("Pilih menu: ")

        when (readLine()?.toIntOrNull()) {
            1 -> tambah_data_transaksi_pakaian(connection)
            2 -> tampilkan_data_transaksi_pakaian(connection)
            3 -> ubah_data_transaksi_pakaian(connection)
            4 -> hapus_data_transaksi_pakaian(connection)
            5 -> break
            else -> println("Pilihan tidak valid. Silakan coba lagi.")
        }
    }
}

fun tambah_data_transaksi_pakaian(connection: Connection) {
    try {
        print("Masukkan ID transaksi baru: ")
        val id_transaksi = readLine()?.toIntOrNull()
        print("Masukkan ID konsumen baru: ")
        val id_konsumen = readLine()?.toIntOrNull()

        if (id_transaksi != null && id_konsumen != null) {
            print("Masukkan tanggal baru (YYYY-MM-DD): ")
            val tanggal_transaksi = readLine()
            print("Masukkan jenis layanan baru: ")
            val jenis_layanan = readLine()
            print("Masukkan berat (dalam kg): ")
            val berat_kg = readLine()?.toDoubleOrNull()
            print("Masukkan total harga: ")
            val total_harga = readLine()?.toDoubleOrNull()
            print("Masukkan status pengiriman: ")
            val status_pengiriman = readLine()

            if (tanggal_transaksi != null && jenis_layanan != null && berat_kg != null && total_harga != null && status_pengiriman != null) {
                val query = """
                    INSERT INTO mengelola_data_transaksi_pakaian 
                    (id_transaksi, id_konsumen, tanggal_transaksi, jenis_layanan, berat_kg, total_harga, status_pengiriman) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """
                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setInt(1, id_transaksi)
                preparedStatement.setInt(2, id_konsumen)
                preparedStatement.setString(3, tanggal_transaksi)
                preparedStatement.setString(4, jenis_layanan)
                preparedStatement.setDouble(5, berat_kg)
                preparedStatement.setDouble(6, total_harga)
                preparedStatement.setString(7, status_pengiriman)

                preparedStatement.executeUpdate()
                println("Data transaksi berhasil ditambahkan.")
            } else {
                println("Input tidak valid. Pastikan semua data terisi dengan benar.")
            }
        } else {
            println("ID transaksi atau ID konsumen tidak valid.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun tampilkan_data_transaksi_pakaian(connection: Connection) {
    try {
        val query = "SELECT * FROM mengelola_data_transaksi_pakaian"
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        println("=== Data Transaksi Pakaian ===")
        while (resultSet.next()) {
            val id_transaksi = resultSet.getInt("id_transaksi")
            val id_konsumen = resultSet.getInt("id_konsumen")
            val jenis_layanan = resultSet.getString("jenis_layanan")
            val tanggal_transaksi = resultSet.getDate("tanggal_transaksi")
            val berat_kg = resultSet.getInt("berat_kg")
            val total_harga = resultSet.getInt("total_harga")
            val status_pengiriman = resultSet.getString("status_pengiriman")
            println("ID: $id_transaksi,id konsumen: $id_konsumen , jenisLayanan: $jenis_layanan,Tanggal: $tanggal_transaksi, Berat pakaian: $berat_kg Total Harga: $total_harga, StatusPengiriman: $status_pengiriman")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun ubah_data_transaksi_pakaian(connection: Connection) {
    try {
        print("Masukkan ID transaksi pakaian yang ingin diubah: ")
        val id_transaksi = readLine()?.toIntOrNull()

        if (id_transaksi != null ) {
            print("Masukkan tanggal (YYYY-MM-DD): ")
            val tanggal_transaksi = readLine()
            print("Masukkan jenis layanan : ")
            val jenis_layanan = readLine()
            print("Masukkan berat (dalam kg): ")
            val berat_kg = readLine()?.toDoubleOrNull()
            print("Masukkan total harga: ")
            val total_harga = readLine()?.toDoubleOrNull()
            print("Masukkan status pengiriman: ")
            val status_pengiriman = readLine()

            if (tanggal_transaksi != null && jenis_layanan != null && berat_kg != null && total_harga != null && status_pengiriman != null) {
                val query = """
           UPDATE mengelola_data_transaksi_pakaian SET tanggal_transaksi = ?, jenis_layanan = ?, berat_kg = ?, total_harga = ?, status_pengiriman = ? 
    WHERE id_transaksi = ?"""

                val preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, tanggal_transaksi)
                preparedStatement.setString(2, jenis_layanan)
                preparedStatement.setDouble(3, berat_kg)
                preparedStatement.setDouble(4, total_harga)
                preparedStatement.setString(5, status_pengiriman)
                preparedStatement.setInt(6, id_transaksi)


                preparedStatement.executeUpdate()
                println("Data transaksi pakaian berhasil diubah.")
            } else {
                println("Input tidak valid. Pastikan semua data terisi dengan benar.")
            }
        } else {
            println("ID transaksi atau ID konsumen tidak valid.")
        }
    } catch (e: Exception){
    e.printStackTrace()
}
}
fun hapus_data_transaksi_pakaian(connection: Connection) {
    try {
        print("Masukkan ID transaksi yang ingin dihapus: ")
        val id_transaksi= readLine()?.toIntOrNull()

        if (id_transaksi != null) {
            val query = "DELETE FROM mengelola_data_transaksi_pakaian WHERE id_transaksi= ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, id_transaksi)
            preparedStatement.executeUpdate()
            println("Data transaksi pakaian berhasil dihapus.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    }

fun mengelola_data_konsumen(connection: Connection) {
    while (true) {
        println("=== Mengelola Data Konsumen ===")
        println("1. Tambah Data Konsumen")
        println("2. Tampilkan Semua Data Konsumen")
        println("3. Ubah Data Konsumen")
        println("4. Hapus Data Konsumen")
        println("5. Kembali ke Menu Utama")
        print("Pilih menu: ")

        when (readLine()?.toIntOrNull()) {
            1 -> tambah_data_konsumen(connection)
            2 -> tampilkan_data_konsumen(connection)
            3 -> ubah_data_konsumen(connection)
            4 -> hapus_data_konsumen(connection)
            5 -> break
            else -> println("Pilihan tidak valid. Silakan coba lagi.")
        }
    }
}
fun tambah_data_konsumen(connection: Connection) {
    try {

        print("Masukkan ID konsumen baru: ")
        val idKonsumen = readLine()?.toIntOrNull()
        if (idKonsumen == null) {
            println("ID konsumen tidak valid. Harus berupa angka.")
            return
        }
        print("Masukkan tanggal registrasi baru (YYYY-MM-DD): ")
        val tanggalRegistrasiInput = readLine()
        val tanggalRegistrasi = try {
            java.sql.Date.valueOf(tanggalRegistrasiInput)
        } catch (e: IllegalArgumentException) {
            println("Tanggal registrasi tidak valid. Format harus YYYY-MM-DD.")
            return
        }

        print("Masukkan nama konsumen baru: ")
        val namaKonsumen = readLine()
        if (namaKonsumen.isNullOrBlank()) {
            println("Nama konsumen tidak boleh kosong.")
            return
        }
        print("Masukkan paket layanan baru: ")
        val paketLayanan = readLine()
        if (paketLayanan.isNullOrBlank()) {
            println("Paket layanan tidak boleh kosong.")
            return
        }
        print("Masukkan alamat baru: ")
        val alamat = readLine()
        if (alamat.isNullOrBlank()) {
            println("Alamat tidak boleh kosong.")
            return
        }
        val query = """
            INSERT INTO mengelola_data_konsumen (id_konsumen, nama_konsumen, tanggal_registrasi, alamat, paket_layanan) 
            VALUES (?, ?, ?, ?, ?)
        """
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setInt(1, idKonsumen)
        preparedStatement.setString(2, namaKonsumen)
        preparedStatement.setDate(3, tanggalRegistrasi)
        preparedStatement.setString(4, alamat)
        preparedStatement.setString(5, paketLayanan)

        val rowsInserted = preparedStatement.executeUpdate()
        if (rowsInserted > 0) {
            println("Data Konsumen berhasil ditambah.")
        } else {
            println("Gagal menambahkan data konsumen.")
        }

    } catch (e: Exception) {
        println("Terjadi kesalahan: ${e.message}")
        e.printStackTrace()
    }
}
fun tampilkan_data_konsumen(connection: Connection) {
    try {
        val query = "SELECT * FROM mengelola_data_konsumen"
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(query)

        println("=== Data Konsumen ===")
        while (resultSet.next()) {
            val idKonsumen = resultSet.getInt("id_konsumen")
            val namaKonsumen = resultSet.getString("nama_konsumen")
            val alamat = resultSet.getString("alamat")
            val paketLayanan = resultSet.getString("paket_layanan")
            val tanggalRegistrasi = resultSet.getDate("tanggal_registrasi")

            println("ID: $idKonsumen, Nama Konsumen: $namaKonsumen, Alamat: $alamat, Paket Layanan: $paketLayanan, Tanggal Registrasi: $tanggalRegistrasi")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun ubah_data_konsumen (connection: Connection) {
    try {
        print("Masukkan ID konsumen yang ingin diubah: ")
        val idKonsumen = readLine()?.toIntOrNull()

        if (idKonsumen == null) {
            println("ID konsumen tidak valid. Masukkan angka yang sesuai.")
            return
        }

        print("Masukkan tanggal registrasi (YYYY-MM-DD): ")
        val tanggalRegistrasi = readLine()
        if (tanggalRegistrasi.isNullOrEmpty() || !tanggalRegistrasi.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            println("Tanggal registrasi tidak valid. Gunakan format YYYY-MM-DD.")
            return
        }

        print("Masukkan nama konsumen: ")
        val namaKonsumen = readLine()
        if (namaKonsumen.isNullOrEmpty()) {
            println("Nama konsumen tidak boleh kosong.")
            return
        }

        print("Masukkan paket layanan: ")
        val paketLayanan = readLine()
        if (paketLayanan.isNullOrEmpty()) {
            println("Paket layanan tidak boleh kosong.")
            return
        }
        print("Masukkan alamat: ")
        val alamat = readLine()
        if (alamat.isNullOrEmpty()) {
            println("Alamat tidak boleh kosong.")
            return
        }
        val query = """
            UPDATE mengelola_data_konsumen 
            SET tanggal_registrasi = ?, nama_konsumen = ?, paket_layanan = ?, alamat = ? 
            WHERE id_konsumen = ?
        """.trimIndent()
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, tanggalRegistrasi)
        preparedStatement.setString(2, namaKonsumen)
        preparedStatement.setString(3, paketLayanan)
        preparedStatement.setString(4, alamat)
        preparedStatement.setInt(5, idKonsumen)

        val rowsUpdated = preparedStatement.executeUpdate()
        if (rowsUpdated > 0) {
            println("Data konsumen berhasil diubah.")
        } else {
            println("ID konsumen tidak ditemukan.")
        }
    } catch (e: Exception) {
        println("Terjadi kesalahan: ${e.message}")
        e.printStackTrace()
    }
}
fun hapus_data_konsumen(connection: Connection) {
    try {
        print("Masukkan ID konsumen yang ingin dihapus: ")
        val id_konsumen= readLine()?.toIntOrNull()

        if (id_konsumen!= null) {
            val query = "DELETE FROM mengelola_data_konsumen WHERE id_konsumen= ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, id_konsumen)
            preparedStatement.executeUpdate()
            println("Data konsumen berhasil dihapus.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
