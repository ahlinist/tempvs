package club.tempvs.item

import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

/**
 * Service that manages {@link Item} and {@link ItemGroup} instances.
 */
@Transactional
@GrailsCompileStatic
class ItemService {

    Item getItem(Long id) {
        Item.get id
    }

    List<Item> getItemsByPeriod(Period period) {
        Item.findAllByPeriod(period)
    }
}
