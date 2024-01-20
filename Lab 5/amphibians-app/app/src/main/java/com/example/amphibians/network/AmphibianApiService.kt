import com.example.amphibians.network.Amphibian
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Step 1: Create a variable to store the API's base URL
private const val BASE_URL = "https://developer.android.com/courses/pathways/android-basics-kotlin-unit-4-pathway-2/"

// Step 2: Build the Moshi object with Kotlin adapter factory that Retrofit will be using to parse JSON
private val moshi = Moshi.Builder().build()

// Step 3: Build a Retrofit instance using the Moshi converter
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

// Step 4: Implement the AmphibianApiService interface with a suspend function for each API method
interface AmphibianApiService {
    @GET("android-basics-kotlin-unit-4-pathway-2-project-api.json")
    suspend fun getAmphibians(): List<Amphibian> // Assuming Amphibian is your data model class
}

// Step 5: Create an AmphibianApi object to expose a lazy-initialized Retrofit service that uses the AmphibianApiService interface
object AmphibianApi {
    val retrofitService: AmphibianApiService by lazy {
        retrofit.create(AmphibianApiService::class.java)
    }
}
