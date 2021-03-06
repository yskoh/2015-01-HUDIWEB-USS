var links = Array.prototype.slice.call(
   document.querySelectorAll('[data-link]')),
    sections = Array.prototype.slice.call(
  document.querySelectorAll('[data-section]'));

var menu = d('.menu').querySelector('ul'),
    menuBtn = d('.toggle_menu');

window.addEventListener('load',init,false);

function d(el){
  if(el) return document.querySelector(el);
}

function init(){

	if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) 
	{
		document.querySelector('.phoneCallBtn').style.display = 'block'; 
	}
	
  // first time active home
  d('[data-section="home"]').classList.add('show-section');
  d('[data-link="home"]').classList.add('active');

  try{
    // loader();
    navigation();
    // lightModal();
    toggleSections();
  }catch(e){
    alert(e);
  }
}

function toggleSections(){
  Array.prototype.forEach.call(links,function(o,i){
    o.addEventListener('click',function(e){
      var section = d('[data-section="'+this.getAttribute('data-link')+'"]');
      e.preventDefault();
      removeLinks(function(){
        o.classList.add('active');
        section.classList.add('show-section');
        menu.classList.toggle('show_menu');
        if(menu.classList.contains('show_menu')){
          menuBtn.innerHTML = '<i class="fa fa-close"></i>';
        }else{
          menuBtn.innerHTML = '<i class="fa fa-navicon"></i>';
        }
      });
    });
  });
}


function removeLinks(_success){
  Array.prototype.forEach.call(links,function(o,i){
    o.classList.remove('active');});
  Array.prototype.forEach.call(sections,function(o,i){
   o.classList.remove('show-section');
  });
  if(_success()) return _success();
}

function navigation(){
  menuBtn.addEventListener('click',function(){
    menu.classList.toggle('show_menu');
    if(menu.classList.contains('show_menu')){
      menuBtn.innerHTML = '<i class="fa fa-close"></i>';
    }else{
      menuBtn.innerHTML = '<i class="fa fa-navicon"></i>';
    }
  });
}