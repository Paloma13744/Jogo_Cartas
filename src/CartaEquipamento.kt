// Classe CartaEquipamento herda de Carta
class CartaEquipamento(
    nome: String,
    descricao: String,
    private val bonusAtaque: Int,
    private val bonusDefesa: Int
) : Carta(nome, descricao, bonusAtaque, bonusDefesa, "Equipamento") {

    // Metodo para aplicar o bônus de equipamento no monstro
    fun aplicarEquipamento(monstro: CartaMonstro) {
        monstro.receberEquipamento(bonusAtaque, bonusDefesa)
    }

    override fun mostrarInfo(): String {
        return "Equipamento: $nome | Descrição: $descricao | Bônus Ataque: $bonusAtaque | Bônus Defesa: $bonusDefesa"
    }
}
