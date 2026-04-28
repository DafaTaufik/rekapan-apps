# Rekap Laundry — Android App Context

## Overview

Aplikasi Android internal keluarga untuk melihat rekap transaksi laundry dari **Randori App** (POS laundry).
Data mengalir: Randori App → Email → N8N → PostgreSQL → **Backend API → Android App ini**.

Pemakaian **internal keluarga saja**, bukan publik. Semua user punya akses yang sama (tidak ada role/level).

---

## Backend API

- **Base URL (local dev):** `http://10.0.2.2:8080` (dari Android emulator ke localhost PC)
- **Base URL (production):** TBD (belum di-deploy)
- **Auth:** Bearer Token (JWT) — semua protected route wajib kirim header `Authorization: Bearer <access_token>`
- **Access token** expired dalam **1 jam**, **Refresh token** expired dalam **30 hari**

---

## Endpoints

### 🔓 Public (tanpa token)

#### POST `/api/auth/register`

Buat akun baru.

```json
// Request Body
{
  "name": "Dafa",
  "email": "dafa@gmail.com",
  "password": "1234"   // min 4 karakter
}

// Response 201
{
  "access_token": "eyJ...",
  "refresh_token": "eyJ...",
  "user": {
    "id": 1,
    "email": "dafa@gmail.com",
    "name": "Dafa",
    "created_at": "...",
    "updated_at": "..."
  }
}
```

#### POST `/api/auth/login`

Login dengan email & password.

```json
// Request Body
{
  "email": "dafa@gmail.com",
  "password": "1234"
}

// Response 200 — sama persis dengan register
```

#### POST `/api/auth/refresh`

Dapatkan access_token baru dengan refresh_token.

```json
// Request Body
{ "refresh_token": "eyJ..." }

// Response 200
{ "access_token": "eyJ..." }
```

---

### 🔐 Protected (wajib header Authorization: Bearer <access_token>)

#### GET `/api/transactions`

List transaksi dengan filter opsional.

| Query Param | Keterangan                           | Contoh       |
| ----------- | ------------------------------------ | ------------ |
| `date`      | Filter tanggal tertentu (YYYY-MM-DD) | `2026-02-21` |
| `branch_id` | Filter cabang                        | `1`          |
| `status`    | Filter status laundry                | `selesai`    |
| `page`      | Halaman (default: 1)                 | `1`          |
| `limit`     | Jumlah per halaman (default: 20)     | `20`         |

**Sort:** tanggal terbaru dulu, jam paling pagi duluan dalam hari yang sama.

```json
// Response 200
{
  "data": [
    {
      "id": 42,
      "branch_id": 1,
      "no_transaksi": "TRX/260116/01444",
      "tanggal_masuk": "2026-02-21T07:23:00Z",
      "nama_pelanggan": "Budi",
      "status": "selesai",
      "status_pembayaran": "lunas", // "lunas" atau "belum lunas"
      "dp": 0,
      "pelunasan": 50000,
      "subtotal": 50000,
      "biaya_antar_jemput": 0,
      "diskon": 0,
      "diskon_poin": 0,
      "total": 50000,
      "jumlah_kg": 3.5,
      "jumlah_pc": 2,
      "created_at": "..."
    }
  ],
  "total": 150,
  "page": 1,
  "limit": 20
}
```

#### GET `/api/transactions/trx/:trx_id`

Cari transaksi by suffix TRX ID.

- `01444` akan cocok dengan `TRX/260116/01444`

```json
// Response 200
{ "data": { ... } }
```

#### GET `/api/transactions/branch/:branch_id`

List semua transaksi untuk 1 cabang (tanpa filter tanggal).

```json
// Response 200
{ "data": [ ... ] }
```

#### PATCH `/api/transactions/:id/toggle-payment`

Toggle status pembayaran (tanpa request body).

- `belum lunas` → `lunas`
- `lunas` → `belum lunas`

> **Catatan:** `:id` di sini adalah field `id` (integer) dari response list transaksi, bukan `no_transaksi`.

```json
// Response 200
{
  "data": { ... },
  "message": "Payment status updated to: lunas"
}
```

#### GET `/api/summary/daily`

Ringkasan harian per cabang.

| Query Param | Keterangan                    |
| ----------- | ----------------------------- |
| `date`      | YYYY-MM-DD, default: hari ini |
| `branch_id` | Opsional                      |

```json
// Response 200
{
  "data": {
    "date": "2026-02-21",
    "total_transactions": 21,
    "total_revenue": 1250000,
    "total_kg": 78.5,
    "total_pc": 12,
    "total_paid": 15 // jumlah yang sudah lunas
  }
}
```

#### GET `/api/summary/range`

Ringkasan per hari dalam rentang tanggal.

| Query Param  | Keterangan                  |
| ------------ | --------------------------- |
| `start_date` | YYYY-MM-DD, wajib           |
| `end_date`   | YYYY-MM-DD, wajib, inklusif |
| `branch_id`  | Opsional                    |

```json
// Response 200
{
  "data": [
    { "date": "2026-01-01", "total_transactions": 10, "total_revenue": 500000, "total_kg": 30, "total_pc": 5 },
    { "date": "2026-01-02", "total_transactions": 15, "total_revenue": 750000, "total_kg": 45, "total_pc": 8 }
  ],
  "start_date": "2026-01-01",
  "end_date": "2026-01-31"
}
```

#### GET `/api/branches`

List semua cabang dengan statistik. (Data agregasi dari tabel transactions.)

```json
// Response 200
{
  "data": [
    { "branch_id": 1, "total_transactions": 500, "total_revenue": 25000000 },
    { "branch_id": 2, "total_transactions": 320, "total_revenue": 16000000 }
  ]
}
```

---

## Rancangan Layar Android

### 1. Splash / Login Screen

- Jika token masih valid → langsung ke Home
- Jika tidak → tampilkan form Login

### 2. Home Screen

- 3 tombol pilihan cabang: **Dafa Laundry 1**, **Dafa Laundry 2**, **Dafa Laundry 3**
- Hardcode saja, tidak perlu fetch dari API

### 3. Halaman Rekap Harian (setelah pilih cabang)

- **Header ringkasan** (hit `GET /api/summary/daily?branch_id=X&date=YYYY-MM-DD`):
  - Total Transaksi, Total Omzet, Total Kg, Total Pcs, Jumlah Lunas
- **Navigasi tanggal:** tombol ◀ (kemarin) — label tanggal — tombol ▶ (besok) + picker tanggal
- **List transaksi** (hit `GET /api/transactions?branch_id=X&date=YYYY-MM-DD&page=1&limit=20`)
  - Tiap kartu: nama pelanggan, no_transaksi, total, kg, status, status_pembayaran
  - Tombol toggle pembayaran → hit `PATCH /api/transactions/:id/toggle-payment`

---

## Error Handling

| HTTP Code | Artinya                                       |
| --------- | --------------------------------------------- |
| 200 / 201 | Sukses                                        |
| 400       | Request tidak valid (validasi gagal)          |
| 401       | Token tidak ada / expired → redirect ke Login |
| 404       | Data tidak ditemukan                          |
| 409       | Email sudah terdaftar (saat register)         |
| 500       | Error server                                  |

Untuk 401, Android harus coba **refresh token** dulu sebelum redirect ke Login.

---

## Tips Implementasi Android

- Simpan `access_token` dan `refresh_token` di **EncryptedSharedPreferences** atau **DataStore**
- Buat **Retrofit Interceptor** untuk otomatis menambahkan `Authorization: Bearer ...` di setiap request
- Buat **Authenticator** di Retrofit untuk otomatis refresh token saat dapat 401
- Gunakan `10.0.2.2` sebagai host saat testing di emulator (bukan `localhost`)
