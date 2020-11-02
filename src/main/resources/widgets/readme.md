These files provide a set of widgets for use in HABPanel.

__Rapid Wind__

The Rapid Wind widget provides a simple display of wind speed and direction, as provided by Sky or Tempest sensors. This data is different from the regular 1-minute observations.

_Note:_ currently this widget is very basic and only a single sensor is supported at a time. Placing more than one widget on a panel will result in all but the last widget being disabled.

To install:

- copy rapidWind.js to your $OPENHAB/conf/html directory.
- In HABPanel, go to a panel and click the pencil icon next to the panel name.
- Click on "Add Widgets" and then click the gear icon next to "Custom Widgets"
- Click on Import Widget -> Import From File...
- Navigate to rapid-wind-widget.json and select the file.

To use:

- Note the ThingUID for the Sky or Tempest thing. You can find this PaperUI by going to Config -> Things and finding the Sky or Tempest thing in the list. It will look something like "weatherflowsmartweather:sky:HB-00000000:SK-000000000" 
- In HABPanel, go to a panel and click the pencil icon next to the panel name.
- Click on the "Add Widget..." button and select the "Rapid Wind Thing" widget. This will add the widget to the panel.
- Click on the 3 dots icon in the upper right hand portion of the widget. Choose "Edit..." from the popup menu.
- Give the widget a name, and enter the ThingUID for the sensor from the first step into the "Sky or Tempest Thing UID" setting box.
- Click Save. After a few seconds, you should start to see data being updated.
