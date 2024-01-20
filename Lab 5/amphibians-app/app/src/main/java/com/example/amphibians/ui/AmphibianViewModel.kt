import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import kotlinx.coroutines.launch

class AmphibianViewModel : ViewModel() {

    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus>
        get() = _status

    enum class AmphibianApiStatus {
        LOADING,
        ERROR,
        DONE
    }


    private val _amphibians = MutableLiveData<List<Amphibian>>()
    val amphibians: LiveData<List<Amphibian>>
        get() = _amphibians

    private val _amphibian = MutableLiveData<Amphibian>()
    val amphibian: LiveData<Amphibian>
        get() = _amphibian

    init {
        _status.value = AmphibianApiStatus.LOADING
        getAmphibianList()
    }

    fun getAmphibianList() {
        viewModelScope.launch {
            try {
                val response = AmphibianApi.retrofitService.getAmphibians()
                _amphibians.value = response
                _status.value = AmphibianApiStatus.DONE
            } catch (e: Exception) {
                _amphibians.value = emptyList()
                _status.value = AmphibianApiStatus.ERROR
            }
        }
    }

    fun onAmphibianClicked(amphibian: Amphibian) {
        _amphibian.value = amphibian
    }
}
