package c.local.com.example;


/**
 * Created by Abhinav Singh on 17,June,2020
 */
public data class PokemonResponseKT(
    val id: Int,
    val count: Int,
    val next: String,
    val previous: String,
    val results: ArrayList<PokeKT>
)

public data class PokeKT(val name: String, val url: String);
