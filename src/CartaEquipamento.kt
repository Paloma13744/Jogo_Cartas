
class CartaEquipamento(
    nome: String,
    descricao: String,
    val bonusAtaque: Int,
    val bonusDefesa: Int
) : Carta(nome, descricao, 0, 0, "Equipamento") {

    fun aplicar(monstro: CartaMonstro) {
        monstro.ataque += bonusAtaque
        monstro.defesa += bonusDefesa

    }
    override fun obterTipo(): String = "Equipamento"
}
