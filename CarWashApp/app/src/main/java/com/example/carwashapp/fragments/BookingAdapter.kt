import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.R
import com.example.carwashapp.model.Booking

class BookingAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvVehicle: TextView = view.findViewById(R.id.tvVehicleType)
        val tvNote: TextView = view.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.tvDate.text = "Датум: ${booking.date}"
        holder.tvTime.text = "Време: ${booking.time}"
        holder.tvVehicle.text = "Тип: ${booking.vehicleType}"
        holder.tvNote.text = "Забелешка: ${booking.note}"
    }

    override fun getItemCount(): Int = bookings.size
}
