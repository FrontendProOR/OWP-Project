	/*
	function search() {
        var form = document.getElementById('searchForm');
        var formData = new FormData(form);
        var transportation = formData.get('transportation').toLowerCase();
        var destination = formData.get('destination').toLowerCase();
        var category = formData.get('category').toLowerCase();
        var minPrice = formData.get('minPrice');
        var maxPrice = formData.get('maxPrice');
        var minNights = formData.get('minNights');
        var maxNights = formData.get('maxNights');
        var cards = document.querySelectorAll('.col-md-4');

        cards.forEach(function(card) {
            var cardTransportation = card.querySelector('span:nth-child(1)').innerText.toLowerCase();
            var cardDestination = card.querySelector('h5.card-title').innerText.toLowerCase();
            var cardCategory = card.querySelector('.category').innerText.toLowerCase(); 
            var cardPrice = parseFloat(card.querySelector('.arrangment-price').innerText);
            var cardNights = parseInt(card.querySelector('.num-nights').innerText);

            var showCard = true;
            if (transportation && cardTransportation.indexOf(transportation) === -1) {
                showCard = false;
            }
            if (destination && cardDestination.indexOf(destination) === -1) {
                showCard = false;
            }
            if (category && cardCategory.indexOf(category) === -1) { 
                showCard = false;
            }
            var minPriceValue = minPrice ? parseFloat(minPrice) : null;
            var maxPriceValue = maxPrice ? parseFloat(maxPrice) : null;
            if ((minPriceValue !== null && cardPrice < minPriceValue) || (maxPriceValue !== null && cardPrice > maxPriceValue)) {
                showCard = false;
            }
            var minNightsValue = minNights ? parseInt(minNights) : null;
            var maxNightsValue = maxNights ? parseInt(maxNights) : null;
            if ((minNightsValue !== null && cardNights < minNightsValue) || (maxNightsValue !== null && cardNights > maxNightsValue)) {
                showCard = false;
            }
            if (showCard) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        });
        filterAndSort(); 
    }

    function filterAndSort() {
        var selectedOption = document.getElementById('sortSelect').value;
        var cardsContainer = document.querySelector('.row');
        var cards = cardsContainer.querySelectorAll('.col-md-4');
        var cardsArray = Array.from(cards);

        switch (selectedOption) {
            case 'destination':
                cardsArray.sort((a, b) => a.querySelector('h5.card-title').innerText.localeCompare(b.querySelector('h5.card-title').innerText));
                break;
            case 'price':
                cardsArray.sort((a, b) => parseFloat(a.querySelector('.arrangment-price').innerText) - parseFloat(b.querySelector('.arrangment-price').innerText));
                break;
            case 'nights':
                cardsArray.sort((a, b) => {
                    var aNights = parseInt(a.querySelector('.num-nights').innerText);
                    var bNights = parseInt(b.querySelector('.num-nights').innerText);
                    return aNights - bNights;
                });
                break;
            default:
                break;
        }

        cardsContainer.innerHTML = '';

        cardsArray.forEach(card => {
            cardsContainer.appendChild(card);
        });
    }
	*/
	function clearForm() {
			window.location.href = "/owpproject";
		}