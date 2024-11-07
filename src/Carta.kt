// Classe abstrata
abstract class Carta(
    protected val nome: String,
    protected val descricao: String,
    protected val ataque: Int,
    protected val defesa: Int
) {
    abstract fun mostrarInfo(): String

    fun getNome(): String = nome
    fun getDescricao(): String = descricao
}





