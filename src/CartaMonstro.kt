
enum class MonstroState {
    ATAQUE, DEFESA
}

// Classe para a carta monstro
class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int
) : Carta(nome, descricao, ataque, defesa, tipo = "Monstro") {

    var estado: MonstroState = MonstroState.DEFESA  // estado inicial
    var jaAtacou: Boolean = false


    fun alternarEstado() {
        estado = if (estado == MonstroState.ATAQUE)
            MonstroState.DEFESA
        else MonstroState.ATAQUE
    }

    override fun obterTipo(): String = "Monstro"
}
