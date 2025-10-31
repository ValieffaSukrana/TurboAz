package com.example.turboazapp.util

import com.example.turboazapp.data.remote.CarDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSeeder @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun seedCarsData(): Result<String> {
        return try {
            val cars = getSampleCars()
            var addedCount = 0

            cars.forEach { car ->
                firestore.collection(Constants.CARS_COLLECTION)
                    .add(car)
                    .await()
                addedCount++
            }

            Result.success("$addedCount maşın uğurla əlavə edildi!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Firebase-də data var mı yoxla
    suspend fun isDataSeeded(): Boolean {
        return try {
            val snapshot = firestore.collection(Constants.CARS_COLLECTION)
                .limit(1)
                .get()
                .await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    // Bütün data-ları sil (test üçün)
    suspend fun clearAllData(): Result<String> {
        return try {
            val snapshot = firestore.collection(Constants.CARS_COLLECTION)
                .get()
                .await()

            snapshot.documents.forEach { document ->
                document.reference.delete().await()
            }

            Result.success("Bütün data silindi!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getSampleCars(): List<CarDto> {
        val currentTime = System.currentTimeMillis()
        val oneDay = 86400000L

        return listOf(
            CarDto(
                brand = "BMW",
                model = "X5",
                year = 2020,
                price = 75000.0,
                currency = "AZN",
                mileage = 45000,
                city = "Bakı",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 3.0,
                horsePower = 340,
                color = "Qara",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "İdeal vəziyyətdə BMW X5. Bütün xidmətlər vaxtında görülüb. Tam təchizatlı, qəza-rəngsiz.",
                images = listOf(
                    "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800",
                    "https://images.unsplash.com/photo-1617531653520-bd4f0f1b0c8d?w=800"
                ),
                sellerId = "seller_1",
                sellerName = "Rəşad Məmmədov",
                phone = "+994501234567",
                createdAt = currentTime - (oneDay * 1),
                updatedAt = currentTime - (oneDay * 1),
                isNew = false,
                viewCount = 125,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Mercedes-Benz",
                model = "E-Class",
                year = 2019,
                price = 55000.0,
                currency = "AZN",
                mileage = 68000,
                city = "Bakı",
                fuelType = "Dizel",
                transmission = "Avtomatik",
                engineVolume = 2.0,
                horsePower = 194,
                color = "Gümüşü",
                bodyType = "Sedan",
                driveType = "Arxa",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Mercedes E200d, tam təchizatlı. Qəza-rəngsiz, ideal vəziyyətdə. Premium paket.",
                images = listOf(
                    "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800",
                    "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?w=800"
                ),
                sellerId = "seller_2",
                sellerName = "Elvin Əliyev",
                phone = "+994551234567",
                createdAt = currentTime - (oneDay * 2),
                updatedAt = currentTime - (oneDay * 2),
                isNew = false,
                viewCount = 89,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Toyota",
                model = "Camry",
                year = 2021,
                price = 45000.0,
                currency = "AZN",
                mileage = 32000,
                city = "Bakı",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 2.5,
                horsePower = 181,
                color = "Ağ",
                bodyType = "Sedan",
                driveType = "Ön",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Toyota Camry 2021, az yürüşlü, ideal vəziyyətdə. Amerikadan gətirilmiş, bütün sənədlər qaydasındadır.",
                images = listOf(
                    "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800"
                ),
                sellerId = "seller_3",
                sellerName = "Nigar Həsənova",
                phone = "+994701234567",
                createdAt = currentTime - (oneDay * 3),
                updatedAt = currentTime - (oneDay * 3),
                isNew = false,
                viewCount = 203,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Hyundai",
                model = "Tucson",
                year = 2022,
                price = 52000.0,
                currency = "AZN",
                mileage = 18000,
                city = "Gəncə",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 2.0,
                horsePower = 150,
                color = "Qırmızı",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "2022-ci il Hyundai Tucson, az işlənmiş, zəmanətli. Rəsmi dilerdən alınıb.",
                images = listOf(
                    "https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=800"
                ),
                sellerId = "seller_4",
                sellerName = "Tural Qasımov",
                phone = "+994771234567",
                createdAt = currentTime - (oneDay * 4),
                updatedAt = currentTime - (oneDay * 4),
                isNew = false,
                viewCount = 156,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Kia",
                model = "Sportage",
                year = 2023,
                price = 58000.0,
                currency = "AZN",
                mileage = 8500,
                city = "Bakı",
                fuelType = "Hibrid",
                transmission = "Avtomatik",
                engineVolume = 1.6,
                horsePower = 230,
                color = "Göy",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "2023 Kia Sportage Hybrid, ən son model, sıfır kimi. Yanacaq sərfiyyatı çox azdır.",
                images = listOf(
                    "https://images.unsplash.com/photo-1609521263047-f8f205293f24?w=800"
                ),
                sellerId = "seller_5",
                sellerName = "Sənan Əhmədov",
                phone = "+994501237890",
                createdAt = currentTime - (oneDay * 5),
                updatedAt = currentTime - (oneDay * 5),
                isNew = false,
                viewCount = 278,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Audi",
                model = "Q7",
                year = 2021,
                price = 95000.0,
                currency = "AZN",
                mileage = 28000,
                city = "Bakı",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 3.0,
                horsePower = 340,
                color = "Qara",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Audi Q7 Premium Plus paket, tam təchizatlı, lüks interyerli. 7 nəfərlik.",
                images = listOf(
                    "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800"
                ),
                sellerId = "seller_6",
                sellerName = "Kamran Həsənli",
                phone = "+994551237890",
                createdAt = currentTime - (oneDay * 6),
                updatedAt = currentTime - (oneDay * 6),
                isNew = false,
                viewCount = 312,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Nissan",
                model = "Qashqai",
                year = 2020,
                price = 38000.0,
                currency = "AZN",
                mileage = 55000,
                city = "Sumqayıt",
                fuelType = "Benzin",
                transmission = "Mexaniki",
                engineVolume = 1.6,
                horsePower = 114,
                color = "Boz",
                bodyType = "Offroad/SUV",
                driveType = "Ön",
                ownerCount = 2,
                crashHistory = false,
                painted = false,
                description = "Nissan Qashqai 2020, qəza-rəngsiz, orta yürüşlü. İqtisadi avtomobil.",
                images = listOf(
                    "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800"
                ),
                sellerId = "seller_7",
                sellerName = "Aygün Məmmədova",
                phone = "+994701237890",
                createdAt = currentTime - (oneDay * 7),
                updatedAt = currentTime - (oneDay * 7),
                isNew = false,
                viewCount = 167,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Honda",
                model = "CR-V",
                year = 2019,
                price = 42000.0,
                currency = "AZN",
                mileage = 72000,
                city = "Bakı",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 2.4,
                horsePower = 190,
                color = "Qəhvəyi",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Honda CR-V, etibarlı və rahat SUV, ailəvi avtomobil. Texniki vəziyyət əladır.",
                images = listOf(
                    "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=800"
                ),
                sellerId = "seller_8",
                sellerName = "Vüsal İbrahimov",
                phone = "+994771237890",
                createdAt = currentTime - (oneDay * 8),
                updatedAt = currentTime - (oneDay * 8),
                isNew = false,
                viewCount = 98,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Volkswagen",
                model = "Passat",
                year = 2018,
                price = 35000.0,
                currency = "AZN",
                mileage = 95000,
                city = "Mingəçevir",
                fuelType = "Dizel",
                transmission = "Avtomatik",
                engineVolume = 2.0,
                horsePower = 150,
                color = "Ağ",
                bodyType = "Sedan",
                driveType = "Ön",
                ownerCount = 1,
                crashHistory = false,
                painted = true,
                description = "VW Passat 2018, dizel mühərrik, ön bamper rənglənib. Qalan hissələr original.",
                images = listOf(
                    "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=800"
                ),
                sellerId = "seller_9",
                sellerName = "Fərid Quliyev",
                phone = "+994501238901",
                createdAt = currentTime - (oneDay * 9),
                updatedAt = currentTime - (oneDay * 9),
                isNew = false,
                viewCount = 145,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Lexus",
                model = "RX 350",
                year = 2021,
                price = 110000.0,
                currency = "AZN",
                mileage = 22000,
                city = "Bakı",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 3.5,
                horsePower = 295,
                color = "Qızılı",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Lexus RX 350 Luxury paket, VIP klass avtomobil. Mark Levinson audio sistemi.",
                images = listOf(
                    "https://images.unsplash.com/photo-1621839673705-6617adf9e890?w=800"
                ),
                sellerId = "seller_10",
                sellerName = "İlham Bayramov",
                phone = "+994551238901",
                createdAt = currentTime - (oneDay * 10),
                updatedAt = currentTime - (oneDay * 10),
                isNew = false,
                viewCount = 421,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Mazda",
                model = "CX-5",
                year = 2020,
                price = 41000.0,
                currency = "AZN",
                mileage = 48000,
                city = "Bakı",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 2.5,
                horsePower = 194,
                color = "Qırmızı",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Mazda CX-5 2020, sportiv dizaynlı SUV. Skyactiv texnologiyası ilə aşağı yanacaq sərfi.",
                images = listOf(
                    "https://images.unsplash.com/photo-1554744512-d6c603f27c54?w=800"
                ),
                sellerId = "seller_11",
                sellerName = "Leyla Əliyeva",
                phone = "+994501239012",
                createdAt = currentTime - (oneDay * 11),
                updatedAt = currentTime - (oneDay * 11),
                isNew = false,
                viewCount = 187,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Ford",
                model = "Focus",
                year = 2017,
                price = 28000.0,
                currency = "AZN",
                mileage = 112000,
                city = "Şəki",
                fuelType = "Benzin",
                transmission = "Mexaniki",
                engineVolume = 1.6,
                horsePower = 125,
                color = "Göy",
                bodyType = "Sedan",
                driveType = "Ön",
                ownerCount = 2,
                crashHistory = false,
                painted = false,
                description = "Ford Focus 2017, mexaniki sürət qutusu, iqtisadi avtomobil. Bütün texniki baxışlar aparılıb.",
                images = listOf(
                    "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800"
                ),
                sellerId = "seller_12",
                sellerName = "Orxan Mustafayev",
                phone = "+994771239012",
                createdAt = currentTime - (oneDay * 12),
                updatedAt = currentTime - (oneDay * 12),
                isNew = false,
                viewCount = 76,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Chevrolet",
                model = "Cruze",
                year = 2016,
                price = 24000.0,
                currency = "AZN",
                mileage = 135000,
                city = "Lənkəran",
                fuelType = "Benzin",
                transmission = "Avtomatik",
                engineVolume = 1.8,
                horsePower = 141,
                color = "Ağ",
                bodyType = "Sedan",
                driveType = "Ön",
                ownerCount = 3,
                crashHistory = false,
                painted = true,
                description = "Chevrolet Cruze 2016, avtomatik, qapaqlar rənglənib. Motor və mühərrik problemsizdir.",
                images = listOf(
                    "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800"
                ),
                sellerId = "seller_13",
                sellerName = "Ramin Nəbiyev",
                phone = "+994551239012",
                createdAt = currentTime - (oneDay * 13),
                updatedAt = currentTime - (oneDay * 13),
                isNew = false,
                viewCount = 54,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Renault",
                model = "Duster",
                year = 2019,
                price = 33000.0,
                currency = "AZN",
                mileage = 78000,
                city = "Qəbələ",
                fuelType = "Dizel",
                transmission = "Mexaniki",
                engineVolume = 1.5,
                horsePower = 110,
                color = "Boz",
                bodyType = "Offroad/SUV",
                driveType = "Tam",
                ownerCount = 1,
                crashHistory = false,
                painted = false,
                description = "Renault Duster 2019, tam ötürücü, dağ yolları üçün ideal. Dizel motor çox iqtisadidir.",
                images = listOf(
                    "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800"
                ),
                sellerId = "seller_14",
                sellerName = "Günel Şərifova",
                phone = "+994701239012",
                createdAt = currentTime - (oneDay * 14),
                updatedAt = currentTime - (oneDay * 14),
                isNew = false,
                viewCount = 198,
                status = "ACTIVE"
            ),
            CarDto(
                brand = "Opel",
                model = "Astra",
                year = 2015,
                price = 21000.0,
                currency = "AZN",
                mileage = 148000,
                city = "Sumqayıt",
                fuelType = "Benzin",
                transmission = "Mexaniki",
                engineVolume = 1.6,
                horsePower = 115,
                color = "Qara",
                bodyType = "Hetçbek",
                driveType = "Ön",
                ownerCount = 2,
                crashHistory = true,
                painted = true,
                description = "Opel Astra 2015, arxada kiçik qəza olub, tam təmir edilib. Texniki vəziyyəti yaxşıdır.",
                images = listOf(
                    "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800"
                ),
                sellerId = "seller_15",
                sellerName = "Emil Həsənov",
                phone = "+994501230123",
                createdAt = currentTime - (oneDay * 15),
                updatedAt = currentTime - (oneDay * 15),
                isNew = false,
                viewCount = 112,
                status = "ACTIVE"
            )
        )
    }
}