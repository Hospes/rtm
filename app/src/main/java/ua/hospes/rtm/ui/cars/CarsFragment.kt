package ua.hospes.rtm.ui.cars

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_cars.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.ui.AbsFragment
import ua.hospes.rtm.core.ui.SpaceItemDecoration
import ua.hospes.rtm.domain.cars.Car
import javax.inject.Inject

class CarsFragment : AbsFragment(), CarsContract.View {
    @Inject lateinit var presenter: CarsPresenter
    private val adapter = CarsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun setActionBarTitle(): Int = R.string.cars_title


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.fragment_cars, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setHasFixedSize(true)
        list.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.cars_column_count))
        list.addItemDecoration(SpaceItemDecoration(resources, R.dimen.grid_item_margin))
        list.adapter = adapter
        adapter.setOnItemClickListener { car, _ -> showEditCarDialog(car) }

        presenter.attachView(this)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    //region ActionBar Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.cars, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> showEditCarDialog(null).let { true }
        R.id.action_clear -> showClearDialog().let { true }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion


    override fun onData(cars: List<Car>) = adapter.submit(cars)

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()


    private fun showEditCarDialog(car: Car?) = EditCarDialogFragment.newInstance(car).show(childFragmentManager, "add_car")

    private fun showClearDialog() = AlertDialog.Builder(context!!)
            .setMessage(R.string.cars_remove_all)
            .setPositiveButton(R.string.yes) { _, _ -> presenter.removeAll() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
}