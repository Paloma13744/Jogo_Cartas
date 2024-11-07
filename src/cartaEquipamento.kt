// classe para a carta equipamento
class cartaEquipamento(
    nome: String,
    descricao: String,
    private val bonusAtaque: Int,
    private val bonusDefesa: Int
) : Carta(nome, descricao, bonusAtaque, bonusDefesa) {

    fun getBonusAtaque(): Int = bonusAtaque
    fun getBonusDefesa(): Int = bonusDefesa

    override fun mostrarInfo(): String {
        return "Equipamento: $nome | Bônus Ataque: $bonusAtaque | Bônus Defesa: $bonusDefesa"
    }


}

