package dev.joshhalvorson.weather.screens.weatherScreen.view

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import dev.joshhalvorson.weather.R
import dev.joshhalvorson.weather.ui.WeatherTheme
import dev.joshhalvorson.weather.screens.weatherScreen.model.Daily
import dev.joshhalvorson.weather.screens.weatherScreen.model.Hourly
import dev.joshhalvorson.weather.screens.weatherScreen.model.WeatherReturn
import dev.joshhalvorson.weather.screens.weatherScreen.viewmodel.WeatherViewModel
import dev.joshhalvorson.weather.ui.typography
import dev.joshhalvorson.weather.util.Constants
import javax.inject.Inject
import dev.joshhalvorson.weather.util.getDayFromUnixSeconds
import dev.joshhalvorson.weather.util.getTimeFromUnixSeconds
import java.lang.reflect.Type

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val weatherViewModel: WeatherViewModel by viewModels()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it?.let { location ->
                setContent {
                    MyApp {
                        BodyContent(
                            weatherViewModel = weatherViewModel,
                            location = location
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    WeatherTheme {
        content()
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BodyContent(
        weatherViewModel: WeatherViewModel,
        color: Color = MaterialTheme.colors.background,
        location: Location,
        modifier: Modifier = Modifier
) {

    val weather: WeatherReturn? by weatherViewModel.getWeather(
            lat = location.latitude,
            lon = location.longitude
    ).observeAsState()

    Surface(color = color) {
        weather?.let { weatherReturn ->
            Column {
                CurrentForecastCard(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        weatherReturn = weatherReturn
                )
                Spacer(modifier = Modifier.height(10.dp))
                DailyForecastList(
                        modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 50.dp, end = 40.dp),
                        weatherReturn = weatherReturn
                )
            }
        }
    }
}

@Composable
private fun CurrentForecastCard(modifier: Modifier = Modifier, weatherReturn: WeatherReturn) {
    Card(
            modifier = modifier,
            shape = RoundedCornerShape(bottomLeft = 35.dp, bottomRight = 35.dp)
    ) {
        Column {
            CurrentWeatherBox(
                    modifier = Modifier.weight(3f).fillMaxWidth().fillMaxHeight(),
                    weatherReturn = weatherReturn
            )
            HourlyForecastList(
                    modifier = Modifier.Companion.weight(1f).padding(start = 10.dp, end = 10.dp),
                    weatherReturn = weatherReturn
            )
        }
    }
}

@Composable
fun CurrentWeatherBox(modifier: Modifier = Modifier, weatherReturn: WeatherReturn) {
    Box(modifier = modifier) {
        Row {
            CurrentWeatherInfo(
                    modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                    weatherReturn = weatherReturn
            )
            CurrentWeatherImage(
                    modifier = Modifier.weight(1f).fillMaxHeight().fillMaxHeight(),
                    weatherReturn = weatherReturn
            )
        }
    }
}

@Composable
fun CurrentWeatherInfo(modifier: Modifier = Modifier, weatherReturn: WeatherReturn) {
    Column(modifier = modifier) {
        Text(
                modifier = Modifier.padding(top = 90.dp, start = 10.dp).gravity(Alignment.CenterHorizontally),
                text = weatherReturn.current.getTempRounded().toString() + Constants.Symbols.DEGREE,
                fontSize = TextUnit.Sp(70),
                style = TextStyle(
                        fontWeight = FontWeight.SemiBold
                )
        )
        Text(
                modifier = Modifier.gravity(Alignment.CenterHorizontally),
                text = weatherReturn.daily[0].temp.getMaxRounded().toString() +
                        Constants.Symbols.DEGREE +
                        "/" +
                        weatherReturn.daily[0].temp.getMinRounded().toString() +
                        Constants.Symbols.DEGREE
        )
    }
}

@Composable
fun CurrentWeatherImage(modifier: Modifier = Modifier, weatherReturn: WeatherReturn) {
    Box(modifier = modifier) {
        val weatherImage = getWeatherIcon(iconName = weatherReturn.current.weather[0].icon)
        Image(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                asset = weatherImage
        )
    }
}

@Composable
fun HourlyForecastList(modifier: Modifier = Modifier, weatherReturn: WeatherReturn) {
    LazyRowFor(
            modifier = modifier,
            items = weatherReturn.hourly.subList(1, 14)
    ) { hourly ->
        HourlyWeatherListItem(
                modifier = Modifier.padding(8.dp),
                hourly = hourly
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun HourlyWeatherListItem(modifier: Modifier = Modifier, hourly: Hourly) {
    val weatherImage = getWeatherIcon(iconName = hourly.weather[0].icon)
    Column (modifier = modifier) {
        Image(
                modifier = Modifier.size(30.dp).weight(1f).gravity(Alignment.CenterHorizontally),
                asset = weatherImage
        )
        Text(
                modifier = Modifier.weight(1f).gravity(Alignment.CenterHorizontally),
                text = hourly.getTempRounded().toString() + Constants.Symbols.DEGREE
        )
        Text(
                modifier = Modifier.weight(1f).gravity(Alignment.CenterHorizontally),
                text = hourly.dt.toLong().getTimeFromUnixSeconds()
        )
    }
}

@Composable
fun DailyForecastList(modifier: Modifier = Modifier, weatherReturn: WeatherReturn) {
    LazyColumnFor(
            items = weatherReturn.daily.subList(1, weatherReturn.daily.lastIndex),
            modifier = modifier
    ) { daily ->
        DailyWeatherListItem(
                modifier = Modifier.fillParentMaxWidth().padding(top = 10.dp),
                daily = daily
        )
    }
}

@Composable
fun DailyWeatherListItem(modifier: Modifier = Modifier, daily: Daily) {
    val weatherIcon = getWeatherIcon(iconName = daily.weather[0].icon)

    Row(modifier = modifier) {
        Text(
                modifier = Modifier.weight(2f),
                text = daily.dt.toLong().getDayFromUnixSeconds(),
                fontSize = TextUnit.Sp(19)
        )
        Image(
                modifier = Modifier.weight(1f).size(30.dp),
                asset = weatherIcon
        )
        Text(
                modifier = Modifier.weight(1f),
                text = daily.temp.getMaxRounded().toString() +
                        Constants.Symbols.DEGREE +
                        "/" +
                        daily.temp.getMinRounded().toString() +
                        Constants.Symbols.DEGREE,
                fontSize = TextUnit.Sp(19)
        )
    }
}

@Composable
fun getWeatherIcon(iconName: String): ImageAsset {
    return when (iconName) {
        "01d" -> imageResource(id = R.drawable.weather_01d)
        "01n" -> imageResource(id = R.drawable.weather_01n)
        "02d" -> imageResource(id = R.drawable.weather_02d)
        "02n" -> imageResource(id = R.drawable.weather_02n)
        "03d" -> imageResource(id = R.drawable.weather_03d)
        "03n" -> imageResource(id = R.drawable.weather_03n)
        "04d" -> imageResource(id = R.drawable.weather_04d)
        "04n" -> imageResource(id = R.drawable.weather_04n)
        "09d" -> imageResource(id = R.drawable.weather_09d)
        "09n" -> imageResource(id = R.drawable.weather_09n)
        "10d" -> imageResource(id = R.drawable.weather_10d)
        "10n" -> imageResource(id = R.drawable.weather_10n)
        "11d" -> imageResource(id = R.drawable.weather_11d)
        "11n" -> imageResource(id = R.drawable.weather_11n)
        "13d" -> imageResource(id = R.drawable.weather_13d)
        "13n" -> imageResource(id = R.drawable.weather_13n)
        "50d" -> imageResource(id = R.drawable.weather_50d)
        "50n" -> imageResource(id = R.drawable.weather_50n)
        else -> imageResource(id = R.drawable.weather_01d)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    MyApp {
//        BodyContent()
//    }
}