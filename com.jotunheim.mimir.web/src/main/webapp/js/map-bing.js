function showMap(mapDivId, longitude, latitude, city) {
	var map = new Microsoft.Maps.Map(document.getElementById(mapDivId), 
        {
            credentials:"AgRfLDspGNHAeYlZVFZtk5tBpVzYayNZTpmCM8JwVh6CAZDQfPqaIJwr-MIi-xdd",
            center: new Microsoft.Maps.Location(latitude, longitude),
            mapTypeId: Microsoft.Maps.MapTypeId.road,
            zoom:13
        }
    );
}