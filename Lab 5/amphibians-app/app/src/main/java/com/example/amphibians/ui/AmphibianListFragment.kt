class AmphibianListFragment : Fragment() {

    private lateinit var viewModel: AmphibianViewModel
    private lateinit var binding: FragmentAmphibianListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_amphibian_list, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(AmphibianViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getAmphibianList() // Initiate the API request

        return binding.root
    }
    // ...
}
