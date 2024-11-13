// Classe base Carta
abstract class Carta(
    val nome: String,
    val descricao: String,
    var ataque: Int,
    var defesa: Int,
    val tipo: String
) {

    open fun obterTipo(): String = "Carta base"
}
