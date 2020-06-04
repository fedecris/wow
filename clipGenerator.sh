ffmpeg -i trailer_00.mp4 -an -vf " fade=type=out:duration=1:start_time=39, scale=1920:1080:force_original_aspect_ratio=increase, crop=1920:1080,  drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=#AA0000@0.4:t=max, drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=black@0.4, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='The Curse Of The Black Pearl (2003)':fontcolor=white:fontsize=75:x=max((w-(t-2.5-10)*w/1.2)\,40):y=940:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dir\: Gore Verbinski':fontcolor=white:fontsize=45:x=max((w-(t-2.5-10)*w/1.2)\,40):y=1010:shadowcolor=black:shadowx=2:shadowy=2, drawbox=enable='between(t,16,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=black@0.4:t=max, drawbox=enable='between(t,16,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=white@0.4:thickness=5, drawtext=enable='between(t,18,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Public\: 8.15':fontcolor=white:fontsize=100:x=150:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,18,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='34.27M votes':fontcolor=white:fontsize=40:x=150:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,20,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Critics\: 7.58':fontcolor=white:fontsize=100:x=150:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,20,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='276 reviews':fontcolor=white:fontsize=40:x=150:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,22,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Final\: 7.86':fontcolor=white:fontsize=150:x=150:y=550:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,24,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Budget\: \$140M':fontcolor=white:fontsize=100:x=1770-tw:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,24,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$195M':fontcolor=white:fontsize=40:x=1770-tw:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,26,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Gross\: \$654M':fontcolor=white:fontsize=100:x=1770-tw:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,26,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$909M':fontcolor=white:fontsize=40:x=1770-tw:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,28,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='R.O.I.\: 367\\\%':fontcolor=white:fontsize=150:x=1770-tw:y=550:shadowcolor=black:shadowx=2:shadowy=2 " -ss 00:00:10.0 -to 00:00:40.0 clip_00.mp4 
ffmpeg -i trailer_01.mp4 -an -vf " fade=type=out:duration=1:start_time=39, scale=1920:1080:force_original_aspect_ratio=increase, crop=1920:1080,  drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=#AA0000@0.4:t=max, drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=black@0.4, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dead Man\'s Chest (2006)':fontcolor=white:fontsize=75:x=max((w-(t-2.5-10)*w/1.2)\,40):y=940:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dir\: Gore Verbinski':fontcolor=white:fontsize=45:x=max((w-(t-2.5-10)*w/1.2)\,40):y=1010:shadowcolor=black:shadowx=2:shadowy=2, drawbox=enable='between(t,28,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=black@0.4:t=max, drawbox=enable='between(t,28,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=white@0.4:thickness=5, drawtext=enable='between(t,30,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Public\: 7.03':fontcolor=white:fontsize=100:x=150:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,30,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2.58M votes':fontcolor=white:fontsize=40:x=150:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,32,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Critics\: 5.10':fontcolor=white:fontsize=100:x=150:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,32,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='282 reviews':fontcolor=white:fontsize=40:x=150:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,34,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Final\: 6.06':fontcolor=white:fontsize=150:x=150:y=550:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,36,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Budget\: \$225M':fontcolor=white:fontsize=100:x=1770-tw:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,36,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$286M':fontcolor=white:fontsize=40:x=1770-tw:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,38,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Gross\: \$1066M':fontcolor=white:fontsize=100:x=1770-tw:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,38,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$1354M':fontcolor=white:fontsize=40:x=1770-tw:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,40,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='R.O.I.\: 374\\\%':fontcolor=white:fontsize=150:x=1770-tw:y=550:shadowcolor=black:shadowx=2:shadowy=2 " -ss 00:00:10.0 -to 00:00:40.0 clip_01.mp4 
ffmpeg -i trailer_02.mp4 -an -vf " fade=type=out:duration=1:start_time=39, scale=1920:1080:force_original_aspect_ratio=increase, crop=1920:1080,  drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=#AA0000@0.4:t=max, drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=black@0.4, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='At World\'s End (2007)':fontcolor=white:fontsize=75:x=max((w-(t-2.5-10)*w/1.2)\,40):y=940:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dir\: Gore Verbinski':fontcolor=white:fontsize=45:x=max((w-(t-2.5-10)*w/1.2)\,40):y=1010:shadowcolor=black:shadowx=2:shadowy=2, drawbox=enable='between(t,40,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=black@0.4:t=max, drawbox=enable='between(t,40,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=white@0.4:thickness=5, drawtext=enable='between(t,42,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Public\: 6.67':fontcolor=white:fontsize=100:x=150:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,42,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='3.41M votes':fontcolor=white:fontsize=40:x=150:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,44,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Critics\: 5.01':fontcolor=white:fontsize=100:x=150:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,44,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='279 reviews':fontcolor=white:fontsize=40:x=150:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,46,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Final\: 5.84':fontcolor=white:fontsize=150:x=150:y=550:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,48,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Budget\: \$300M':fontcolor=white:fontsize=100:x=1770-tw:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,48,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$372M':fontcolor=white:fontsize=40:x=1770-tw:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,50,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Gross\: \$961M':fontcolor=white:fontsize=100:x=1770-tw:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,50,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$1192M':fontcolor=white:fontsize=40:x=1770-tw:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,52,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='R.O.I.\: 220\\\%':fontcolor=white:fontsize=150:x=1770-tw:y=550:shadowcolor=black:shadowx=2:shadowy=2 " -ss 00:00:10.0 -to 00:00:40.0 clip_02.mp4 
ffmpeg -i trailer_03.mp4 -an -vf " fade=type=out:duration=1:start_time=39, scale=1920:1080:force_original_aspect_ratio=increase, crop=1920:1080,  drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=#AA0000@0.4:t=max, drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=black@0.4, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='On Stranger Tides (2011)':fontcolor=white:fontsize=75:x=max((w-(t-2.5-10)*w/1.2)\,40):y=940:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dir\: Rob Marshall':fontcolor=white:fontsize=45:x=max((w-(t-2.5-10)*w/1.2)\,40):y=1010:shadowcolor=black:shadowx=2:shadowy=2, drawbox=enable='between(t,52,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=black@0.4:t=max, drawbox=enable='between(t,52,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=white@0.4:thickness=5, drawtext=enable='between(t,54,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Public\: 5.92':fontcolor=white:fontsize=100:x=150:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,54,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='0.72M votes':fontcolor=white:fontsize=40:x=150:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,56,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Critics\: 4.34':fontcolor=white:fontsize=100:x=150:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,56,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='337 reviews':fontcolor=white:fontsize=40:x=150:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,58,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Final\: 5.13':fontcolor=white:fontsize=150:x=150:y=550:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,60,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Budget\: \$250M':fontcolor=white:fontsize=100:x=1770-tw:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,60,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$285M':fontcolor=white:fontsize=40:x=1770-tw:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,62,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Gross\: \$1046M':fontcolor=white:fontsize=100:x=1770-tw:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,62,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$1192M':fontcolor=white:fontsize=40:x=1770-tw:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,64,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='R.O.I.\: 318\\\%':fontcolor=white:fontsize=150:x=1770-tw:y=550:shadowcolor=black:shadowx=2:shadowy=2 " -ss 00:00:10.0 -to 00:00:40.0 clip_03.mp4 
ffmpeg -i trailer_04.mp4 -an -vf " fade=type=out:duration=1:start_time=39, scale=1920:1080:force_original_aspect_ratio=increase, crop=1920:1080,  drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=#AA0000@0.4:t=max, drawbox=enable='between(t,12,39)':x=20:y=920:h=140:w=iw-(2*20):color=black@0.4, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dead Men Tell No Tales (2017)':fontcolor=white:fontsize=75:x=max((w-(t-2.5-10)*w/1.2)\,40):y=940:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,11,39)':fontfile=/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf:text='Dir\: Joachim Rønning\, Espen Sandberg':fontcolor=white:fontsize=45:x=max((w-(t-2.5-10)*w/1.2)\,40):y=1010:shadowcolor=black:shadowx=2:shadowy=2, drawbox=enable='between(t,64,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=black@0.4:t=max, drawbox=enable='between(t,64,39)':x=100:y=110:w=(iw-200):h=(ih-450):color=white@0.4:thickness=5, drawtext=enable='between(t,66,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Public\: 6.10':fontcolor=white:fontsize=100:x=150:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,66,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='0.40M votes':fontcolor=white:fontsize=40:x=150:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,68,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Critics\: 4.02':fontcolor=white:fontsize=100:x=150:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,68,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='362 reviews':fontcolor=white:fontsize=40:x=150:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,70,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Final\: 5.06':fontcolor=white:fontsize=150:x=150:y=550:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,72,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Budget\: \$230M':fontcolor=white:fontsize=100:x=1770-tw:y=150:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,72,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$241M':fontcolor=white:fontsize=40:x=1770-tw:y=250:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,74,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='Gross\: \$795M':fontcolor=white:fontsize=100:x=1770-tw:y=350:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,74,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='2020\: \$835M':fontcolor=white:fontsize=40:x=1770-tw:y=450:shadowcolor=black:shadowx=2:shadowy=2, drawtext=enable='between(t,76,39)':fontfile=/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf:text='R.O.I.\: 246\\\%':fontcolor=white:fontsize=150:x=1770-tw:y=550:shadowcolor=black:shadowx=2:shadowy=2 " -ss 00:00:10.0 -to 00:00:40.0 clip_04.mp4 
