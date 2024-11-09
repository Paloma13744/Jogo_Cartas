// Classe para a carta monstro
class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int,
    private var vida: Int,
    private var state: Monstrostate = Monstrostate.DEFESA // Estado inicial
) : Carta(nome, descricao, ataque, defesa, tipo = "Monstro") {

    // Calcula o poder
    fun poder(): Int {
        return if (state == Monstrostate.ATAQUE) ataque else defesa
    }

    // Altera o estado do monstro
    fun alterarEstado() {
        state = if (state == Monstrostate.ATAQUE) Monstrostate.DEFESA else Monstrostate.ATAQUE
    }

    // Modo para realizar um ataque
    fun modoAtaque(): Boolean {
        return state == Monstrostate.ATAQUE
    }

    // Modo para se defender
    fun modoDefesa(): Boolean {
        return state == Monstrostate.DEFESA
    }

    // Reduz a vida de um monstro
    fun reduzirVida(dano: Int) {
        vida -= dano
        if (vida < 0) vida = 0
        println("${obterNome()} agora tem $vida de vida!")
    }

    // Equipa o monstro com uma carta de equipamento
    fun equiparCarta(cartaEquipamento: CartaEquipamento) {
        cartaEquipamento.aplicarEquipamento(this) // Aplica os bônus de ataque e defesa
        println("${obterNome()} foi equipado com ${cartaEquipamento.obterNome()}.")
    }

    // Recebe bônus de defesa ou ataque
    fun receberEquipamento(bonusAtaque: Int, bonusDefesa: Int) {
        ataque += bonusAtaque
        defesa += bonusDefesa
        println("${obterNome()} recebeu um bônus de $bonusAtaque no ataque e $bonusDefesa na defesa!")
    }

    // Exibe informações do monstro
    override fun mostrarInfo(): String {
        return "Monstro: ${obterNome()} | Ataque: $ataque | Defesa: $defesa | Vida: $vida | Estado: $state"
    }


}

// Enumeração para o estado do monstro
enum class Monstrostate {
    ATAQUE, DEFESA
}
