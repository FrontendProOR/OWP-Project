package com.ftn.owpproject.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.owpproject.dao.TravelDAO;
import com.ftn.owpproject.model.Travel;
import com.ftn.owpproject.service.TravelService;
@Service
public class DatabaseTravelService implements TravelService {
	@Autowired
	private TravelDAO travelDAO;
	
//	@SuppressWarnings("unused")
//	@Autowired
//	private TravelCategoryDAO travelCategoryDAO;
	
	@Override
	public Travel findOne(Long id) {
		return travelDAO.findOne(id);
	}

	@Override
	public List<Travel> findAll() {
		return travelDAO.findAll();
	}

	@Override
	public Travel save(Travel travel) {
		travelDAO.save(travel);
		return travel;
	}

	@Override
	public boolean hasReservations(Long travelId) {
	    return travelDAO.countReservationsByTravelId(travelId) > 0;
	}

	
	@Override
	public Travel update(Travel travel) {
		travelDAO.update(travel);
		return travel;
	}

	@Override
	public Travel delete(Long id) {
		Travel travel = travelDAO.findOne(id);
		if(travel != null) {
			travelDAO.delete(id);
		}
		return travel;
	}
	
	@Override
	public int updateAvailableSeats(Long travelId, int availableSeats) {
	    return travelDAO.updateAvailableSeats(travelId, availableSeats);
	}

	public double getCurrentPrice(Travel travel) {
        if (travel.getDiscountEndDate() != null && travel.getDiscountEndDate().isBefore(LocalDateTime.now())) {
            return travel.getOriginalPrice();
        } else {
            return travel.getArrangmentPrice();
        }
    }
	
	public void updatePrice(Long travelId, double newPrice) {
        travelDAO.updatePrice(travelId, newPrice);
    }

    public void updateAllTravelPrices() {
        List<Travel> travels = findAll();
        travels.forEach(this::checkAndUpdatePrice);
    }

    private void checkAndUpdatePrice(Travel travel) {
        if (travel.getDiscountEndDate() != null && travel.getDiscountEndDate().isAfter(LocalDateTime.now())) {
            double discount = travel.getOriginalPrice() * (travel.getDiscountPercentage() / 100);
            double newPrice = travel.getOriginalPrice() - discount;
            if (travel.getArrangmentPrice() != newPrice) {
                travel.setArrangmentPrice(newPrice);
                updatePrice(travel.getId(), newPrice);
            }
        } else if (travel.getArrangmentPrice() != travel.getOriginalPrice()) {
            travel.setArrangmentPrice(travel.getOriginalPrice());
            updatePrice(travel.getId(), travel.getOriginalPrice());
        }
    }
}
