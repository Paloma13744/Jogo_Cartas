// Classe para a carta monstro
class CartaMonstro(
    nome: String,
    descricao: String,
    ataque: Int,
    defesa: Int,
    tipo: String,
    private var state: Monstrostate = Monstrostate.DEFESA // Estado inicial
) : Carta(nome, descricao, ataque, defesa, tipo = "Monstro") {

    override fun obterTipo(): String = "Monstro"

    private var vida: Int = 10000 // Valor inicial de vida do monstro

      fun poder(): Int {
        return if (state == Monstrostate.ATAQUE) ataque else defesa
    }

    // Altera o estado do monstro entre ataque e defesa
    fun alterarEstado() {
        state = if (state == Monstrostate.ATAQUE) Monstrostate.DEFESA else Monstrostate.ATAQUE
    }

    // Verifica se o monstro está no modo de ataque
    fun modoAtaque(): Boolean {
        return state == Monstrostate.ATAQUE
    }

    // Verifica se o monstro está no modo de defesa
    fun modoDefesa(): Boolean {
        return state == Monstrostate.DEFESA
    }

    // Reduz a vida do monstro em função do dano recebido
    fun reduzirVida(dano: Int) {
        vida -= dano
        if (vida < 0) vida = 0
        println("${obterNome()} agora tem $vida de vida!")
    }

    // Equipa o monstro com uma carta de equipamento, aplicando bônus de ataque e defesa
    fun equiparCarta(cartaEquipamento: CartaEquipamento) {
        cartaEquipamento.aplicarEquipamento(this) // Aplica os bônus de ataque e defesa
        println("${obterNome()} foi equipado com ${cartaEquipamento.obterNome()}.")
    }

    // Recebe bônus de ataque e defesa de uma carta de equipamento
    fun receberEquipamento(bonusAtaque: Int, bonusDefesa: Int) {
        ataque += bonusAtaque
        defesa += bonusDefesa
        println("${obterNome()} recebeu um bônus de $bonusAtaque no ataque e $bonusDefesa na defesa!")
    }

    // Exibe as informações do monstro
    override fun mostrarInfo(): String {
        return "Monstro: ${obterNome()} | Ataque: $ataque | Defesa: $defesa | Vida: $vida | Estado: $state"
    }
}

// Enumeração para o estado do monstro
enum class Monstrostate {
    ATAQUE, DEFESA
}
