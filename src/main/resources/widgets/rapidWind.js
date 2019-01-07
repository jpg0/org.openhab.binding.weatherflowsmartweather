// An angularjs module for widgets that display WeatherFlow SmartWeather "rapid-wind" events
//
// TODO: Support multiple Sky devices within the service. 
//       Do we need to unregister listeners?
var myModule = angular.module("rapidWind", []);

myModule.service("RapidWindService", ["$rootScope",  "OHService", function($rootScope,  OHService){
                this.$rootScope = $rootScope;
                this.OHService = OHService;
                var onChange = 0;
                var thingUid = 0;

                // this is the type of event, used to filter things we don't care about.
                var eventType = "RapidWindEvent";

		// this is the topic that rapid wind events are published on.
                var targetTopic = "smarthome/things/{uid}/rapidwind";

		this.setOnChange = function(uid, f) { thingUid = uid; targetTopic = targetTopic.replace("{uid}", uid); onChange = f; };

                this.messageReceived = function(event, oh2event) { 

		    if(oh2event.type != eventType) return;
                    if(oh2event.topic == targetTopic && onChange != 0)
		      onChange(oh2event.payload);
	        };
		this.myFunc = function(x) {};

		// habpanel broadcasts OpenHAB events on this "channel".
		$rootScope.$on('openhab-event', this.messageReceived);

		console.log("RapidWindService started.");
	}]);

// Controller that listens to Rapid Wind events for a given thing and updates its model for use by templates.
myModule.controller("RapidWindCtrl", ["$scope", "RapidWindService", function($scope, RapidWindService) {
      RapidWindService.setOnChange( $scope.config.skyUid, function(payload) { 
          $scope.skyName = RapidWindService.OHService.getThingByUid($scope.config.skyUid).label;
          $scope.epoch = payload.epoch;
          $scope.windSpeed = payload.windSpeed;
          $scope.windDirection = payload.windDirection;
      });
  } ]);

