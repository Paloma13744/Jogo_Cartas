// Classe base Carta
abstract class Carta(
    protected val nome: String,
    protected val descricao: String,
    protected var ataque: Int,
    protected var defesa: Int,
    protected val tipo: String
) {
    abstract fun mostrarInfo(): String


    fun obterNome(): String {
        return nome
    }
}
