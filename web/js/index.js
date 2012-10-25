var page = 0;
var limit = 20;

$(function(){
	$('#prev').addClass('disabled');
	fetch();
	autoComplete();
	$('form').submit(function(){
		page = 0;
		$('#prev').addClass('disabled');
		fetch();
		return false;
	});
	$('#next').click(function(){
		page++;
		$('#prev').removeClass('disabled');
		fetch();
		return false;
	});
	$('#prev').click(function(){
		page--;
		if(page==0)
		{
			$('#prev').addClass('disabled');
		}
		fetch();
		return false;
	});
});

function autoComplete()
{
	$.ajax({
		url: 'AutoComplete',
		dataType: 'json',
		type: 'POST',
		success: function(data){
			$('#host').typeahead({source:data.hosts});
			$('#prio').typeahead({source:data.prios});
			$('#prog').typeahead({source:data.progs});
		}
	});
}

function fetch()
{
	var query = {};
	if($('#limit').val().length>0)
	{
		limit = new Number($('#limit').val()).valueOf();
	}
	if($('#host').val().length>0)
	{
		query.HOST = $('#host').val();
	}
	if($('#prio').val().length>0)
	{
		query.PRIORITY = $('#prio').val();
	}
	if($('#prog').val().length>0)
	{
		query.PROGRAM = $('#prog').val();
	}
	$.ajax({
		url: 'Syslog',
		data: {
			query: JSON.stringify(query),
			limit: limit,
			skip: page*limit
		},
		dataType: 'json',
		type: 'POST',
		success: function(data){
			$('tbody').empty();
			for(var x=0;x<data.length;++x)
			{
				if(data[x].PRIORITY == 'warning')
				{
					$('tbody').append('<tr class="warning"><td>'+data[x].DATE+'</td><td>'+data[x].HOST+'</td><td>'+data[x].MESSAGE+'</td><td>'+data[x].PRIORITY+'</td><td>'+data[x].PROGRAM+'</td></tr>');
				}
				else if(data[x].PRIORITY == 'crit' || data[x].PRIORITY == 'err')
				{
					$('tbody').append('<tr class="error"><td>'+data[x].DATE+'</td><td>'+data[x].HOST+'</td><td>'+data[x].MESSAGE+'</td><td>'+data[x].PRIORITY+'</td><td>'+data[x].PROGRAM+'</td></tr>');
				}
				else
				{
					$('tbody').append('<tr><td>'+data[x].DATE+'</td><td>'+data[x].HOST+'</td><td>'+data[x].MESSAGE+'</td><td>'+data[x].PRIORITY+'</td><td>'+data[x].PROGRAM+'</td></tr>');
				}
			}
		}
	});
}
