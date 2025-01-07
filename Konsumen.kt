import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

fun main() {
    val url = "jdbc:mysql://localhost:3306/sistem_manajemen_jasa_laundry"
    val user = "root"
    val password = ""

    try {
        val connection: Connection = DriverManager.getConnection(url, user, password)
        println("Koneksi berhasil")
        println("\n==== Selamat Datang di Laundry Cumalawi(Cucian Makin Mengkilap dan Wangi ====")
        println("\n=== Hari ini Weekend? Laundry Yukk!")


        // Registrasi atau login  sebelum masuk ke menu utama
        var loggedIn = false
        while (!loggedIn) {
            println("\nSilakan Pilih Jika belum punya akun pilih 1 dan jika sudah pilih 2:")
            println("1. Registrasi Akun")
            println("2. Login")
            print("Pilih menu: ")
            when (readLine()?.toIntOrNull()) {
                1 -> registrasi(connection)
                2 -> {
                    print("Masukkan Username: ")
                    val username = readLine().orEmpty()
                    print("Masukkan Password: ")
                    val passwordInput = readLine().orEmpty()
                    if (login(connection, username, passwordInput)) {
                        println("Login berhasil!")
                        loggedIn = true
                        menuUtama(connection)
                    } else {
                        println("Login gagal! Username atau password salah.")
                    }
                }

                else -> println("Pilihan tidak valid. Silakan coba lagi.")
            }

        }

        connection.close()
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun registrasi(connection: Connection) {
    while (true) {
        print("Masukkan ID_Konsumen: ")
        val id_konsumen = readLine()?.toIntOrNull()

        if (id_konsumen == null) {
            println("ID_Konsumen salah. Silakan coba lagi.")
            continue
        }

        print("Masukkan Username: ")
        val username = readLine().orEmpty()
        print("Masukkan Password: ")
        val password = readLine().orEmpty()

        if (username.isBlank() || password.isBlank()) {
            println("Username atau password tidak boleh kosong. Silakan coba lagi.")
            continue
        }

        try {
            val checkQuery = "SELECT * FROM konsumen WHERE id_konsumen = ? OR username = ?"
            val checkStmt = connection.prepareStatement(checkQuery)
            checkStmt.setInt(1, id_konsumen)
            checkStmt.setString(2, username)
            val resultSet = checkStmt.executeQuery()

            if (resultSet.next()) {
                println("ID_Konsumen atau Username sudah terdaftar. Silakan gunakan yang lain.")
                resultSet.close()
                checkStmt.close()
                continue
            }

            resultSet.close()
            checkStmt.close()

            val insertQuery = "INSERT INTO konsumen (id_konsumen, username, password) VALUES (?, ?, ?)"
            val insertStmt = connection.prepareStatement(insertQuery)
            insertStmt.setInt(1, id_konsumen)
            insertStmt.setString(2, username)
            insertStmt.setString(3, password)
            insertStmt.executeUpdate()
            insertStmt.close()

            println("Registrasi berhasil! Akun terdaftar.")
            break
        } catch (e: Exception) {
            println("Terjadi kesalahan: ${e.message}")
            break
        }
    }
}

fun login(connection: Connection, username: String, password: String): Boolean {
    val query = "SELECT * FROM konsumen WHERE username = ? AND password = ?"
    return try {
        val preparedStatement = connection.prepareStatement(query)
        preparedStatement.setString(1, username)
        preparedStatement.setString(2, password)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        val loginSuccess = resultSet.next()
        resultSet.close()
        preparedStatement.close()
        loginSuccess
    } catch (e: Exception) {
        println("Login gagal: ${e.message}")
        false
    }
}


fun menuUtama(connection: Connection) {
    var menuAktif = true
    while (menuAktif) {
        println("\nMenu Utama:")
        println("1. Cek Status Laundry")
        println("2. Logout")
        print("Pilih menu: ")
        when (readLine()?.toIntOrNull()) {
            1 -> cek_status_laundry(connection)
            2 -> {
                menuAktif = false
                println("Logout berhasil. Anda telah keluar dari sistem. Sampai jumpaa.")
            }

            else -> println("Pilihan tidak valid. Silakan coba lagi.")
        }
    }
}

fun cek_status_laundry(connection: Connection) {
    try {
        print("Masukkan ID_Konsumen yang ingin dicek: ")
        val id_konsumen = readLine()?.toIntOrNull() // Input ID_Konsumen

        if (id_konsumen != null) {
            // Query untuk mencari status laundry berdasarkan ID_Konsumen
            val query = "SELECT status_pengiriman FROM cek_status_laundry WHERE id_konsumen = ?"
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setInt(1, id_konsumen)

            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                val status_pengiriman = resultSet.getString("status_pengiriman")
                println("Status Pengiriman untuk ID_Konsumen $id_konsumen: $status_pengiriman")
            } else {
                println("Tidak ditemukan data untuk ID_Konsumen $id_konsumen.")
            }
            resultSet.close()
            preparedStatement.close()
        } else {
            println("Input tidak valid. Silakan masukkan ID_Konsumen yang benar.")
        }
    } catch (e: Exception) {
        println("Terjadi kesalahan: ${e.message}")
    }
}