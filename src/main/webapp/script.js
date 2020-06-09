


google.charts.load('current', {packages: ['corechart', 'line']});
google.charts.setOnLoadCallback(drawBasic);



function drawBasic() {

      var data = new google.visualization.DataTable();
      data.addColumn('string', 'X');
      data.addColumn('number', 'Temperature');
      data.addRows([
    	  [# th:each="day : ${weather}"]
    	[ '[[${#temporals.format(day.weatherTime,'HH:mm')}]]' , [[${day.temperature}]] ], 
   
    	 [/]

      ]);

      
      var options = {
        hAxis: {
          title: 'Time'
        },
        vAxis: {
          title: 'Temperature'
        },
        legend: {position: 'none'}

      };

      var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

      chart.draw(data, options);
    }

